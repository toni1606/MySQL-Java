package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
//	Login Data
	private String url;
	private String username;
	private String password;

//	Connection
	private Connection connection;

//	Tables
	private Table[] tables;

//	Construtor
	public Database(String url, String username, String password) throws SQLException {
		this.url = url;
		this.username = username;
		this.password = password;

		connection = DriverManager.getConnection(url, username, password);

		readTablesFromDatabase();
	}

//	Getter
	public String getUrl() {
		return url;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Table[] getTables() {
		return tables;
	}

	public Table getTable(int index) {
		Table out;
		if(index < tables.length && index >= 0)
			out = tables[index];
		else
			out = null;
		return out;
	}

//	INSERT, UPDATE, DELETE Instance
	public void insert(Table t, String[] values) throws SQLException {
		if(isValidTable(t)) {
			Statement statement = connection.createStatement();

			StringBuilder sql = new StringBuilder("INSERT INTO " + t.getName() + "(");

			// List all columns to be added
			Column[] tableColumns = t.getColumns();
			for(int i = 0; i < tableColumns.length; i++) {
				sql.append(tableColumns[i].getName());

				if(i != tableColumns.length - 1)
					sql.append(',');
			}

			sql.append(") VALUES (");

			// List values to be inserted
			for(int i = 0; i < values.length; i++) {
				sql.append(values[i]);

				if(i != values.length - 1)
					sql.append(',');
			}

			sql.append(')');

			// Execute insert Statement
			statement.executeUpdate(sql.toString());

			// Close statement
			statement.close();
		}
	}

	public void updateInstance(Table t, String[] values, String condition) throws SQLException {
		if(isValidTable(t)) {
			Statement statement = connection.createStatement();

			StringBuilder sql = new StringBuilder("UPDATE " + t.getName() + " SET ");

			// Add every value to be changed to statement
			for(int i = 0; i < values.length; i++) {
				sql.append(values[i]);

				if(i < values.length - 1)
					sql.append(", ");
			}

			// Add condition to statement
			sql.append("WHERE " + condition);

			statement.executeUpdate(sql.toString());
			statement.close();
		}
	}

	public void deleteInstance(Table t, String condition) throws SQLException {
		if(isValidTable(t)) {
			Statement statement = connection.createStatement();

			StringBuilder sql = new StringBuilder("DELETE FROM " + t.getName());

			if(condition != null)
				sql.append(" WHERE " + condition);

			statement.executeUpdate(sql.toString());
			statement.close();
		}
	}

//	SELECT Methods
	public String[] selectRow(Table t, int index) throws SQLException {
		return searchFor(t, t.getPrimaryKey(0), Integer.toString(index));
	}

	public String[][] selectAll(Table t) throws SQLException {
		if(isValidTable(t)) {
			Statement statement = connection.createStatement();
			Statement statement1 = connection.createStatement();
			ResultSet resultSet;

			// Get Columns in Table and create a String[][] Array to hold the exact amount of Data
			resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + t.getName());
			resultSet.next();

			String[][] values = new String[Integer.parseInt(resultSet.getString(1))][t.getColumns().length];

			// Executes Querry and saves output in resultSet
			resultSet = statement1.executeQuery("SELECT * FROM " + t.getName());

			// Row Counter
			int i = 0;
			while(resultSet.next()) {
				// Saves value of each column in its position
				for(int j = 0; j < t.getColumns().length; j++) {
					values[i][j] = resultSet.getString(j + 1);
				}
				i++;
			}

			resultSet.close();
			statement.close();
			statement1.close();

			return values;
		}
		return null;
	}

//	Search Method
	public String[] searchFor(Table t, Column c, String value) throws SQLException {
		if(isValidTable(t) && isValidColumn(t, c)) {
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery("SELECT * FROM " + t.getName() + " WHERE " + c.getName() + " = " + value);
			resultSet.next();

			String[] row = new String[t.getColumns().length];
			// Save Column Data in Array
			for(int i = 0; i < row.length; i++) {
				// ResultSet index beginns at 1 so it is always +1 of i
				row[i] = resultSet.getString(i + 1);
			}

			statement.close();
			resultSet.close();

			return row;
		}

		return null;
	}

//	Helper Methods
	private boolean isValidTable(Table t) {
		for(Table table : tables) {
			if(table.equals(t))
				return true;
		}

		return false;
	}
	private boolean isValidColumn(Table t, Column c) {
		for(Column column : t.getColumns()) {
			if(column.equals(c))
				return true;
		}

		return false;
	}

	private void readTablesFromDatabase() throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SHOW TABLES");
		List<Table> tem = new ArrayList<>();

		while (resultSet.next()) {
			tem.add(new Table(resultSet.getString(1)));
		}

		// Fills Columns (exept ForeignKeys) for every Table
		// ForeignKeys need to have a referencedColumn, and so you have to have all other columns in place before you can set them
		for(Table t : tem) {
			resultSet = statement.executeQuery("SHOW COLUMNS FROM " + t.getName());

			List<Column> c = new ArrayList<>();
			List<Column> priK = new ArrayList<>();
			List<ForeignKey> forK = new ArrayList<>();
			while(resultSet.next()) {
				// Gets Name & Datatype for Column and saves it in the columns temporary Array
				Column col = new Column(resultSet.getString(1), resultSet.getString(2));
				c.add(col);

				if(resultSet.getString(4).equals("PRI")) {
					priK.add(col);
				}
			}

			// Converts List<> to Array and sets the Table Fields
			t.setColumns(c.toArray(new Column[c.size()]));
			t.setPrimaryKeys(priK.toArray(new Column[priK.size()]));
		}

		// It is needed to reLoop over the Tables
		for(Table t : tem) {
			resultSet = statement.executeQuery("select k.COLUMN_NAME, k.CONSTRAINT_NAME, k.REFERENCED_COLUMN_NAME, k.REFERENCED_TABLE_NAME, c.DATA_TYPE from information_schema.KEY_COLUMN_USAGE k join INFORMATION_SCHEMA.COLUMNS c on k.COLUMN_NAME = c.COLUMN_NAME where k.TABLE_NAME = \"" + t.getName() + "\" and k.CONSTRAINT_NAME NOT IN (\"PRIMARY\");");

			List<ForeignKey> forK = new ArrayList<>();
			while(resultSet.next()) {
				// Checks whole Column array for the referenced Column and saves it in kfCol
				Column kfCol = null;
				for (Column temCol : t.getColumns()) {
					if (temCol.getName().equals(resultSet.getString(3))) {
						kfCol = temCol;
						break;
					}
				}

				// Adds ForeinKey to ForeignKey Array
				forK.add(new ForeignKey(resultSet.getString(1), resultSet.getString(5), t, kfCol, resultSet.getString(2)));
			}
			t.setForeignKeys(forK.toArray(new ForeignKey[forK.size()]));
		}

		// Closes all statements and ResultSets
		resultSet.close();
		statement.close();

		// Converts List<Table> to Array and saves it in the tables Array
		tables = tem.toArray(new Table[tem.size()]);
	}

//	Close Connection
	public void close() throws SQLException {
		connection.close();
	}
}