package database;

import java.util.Arrays;
import java.util.Objects;

public class Table {
	private String name;

//	Columns
	private Column[] columns;
	private Column[] primaryKeys;
	private ForeignKey[] foreignKeys;

//	Constructors
	public Table(String name) {
		this.name = name;
	}

	public Table(String name, Column[] columns, Column[] primaryKeys) {
		this.name = name;
		this.columns = columns;
		this.primaryKeys = primaryKeys;
	}

	public Table(String name, Column[] columns, Column[] primaryKeys, ForeignKey[] foreignKeys) {
		this.name = name;
		this.columns = columns;
		this.primaryKeys = primaryKeys;
		this.foreignKeys = foreignKeys;
	}

//	Getter
	public String getName() {
		return name;
	}

	public Column[] getColumns() {
		return columns;
	}

	public Column getColumn(int index) {
		Column out;
		if(index < columns.length && index >= 0)
			out = columns[index];
		else
			out = null;
		return out;
	}

	public Column[] getPrimaryKeys() {
		return primaryKeys;
	}

	public Column getPrimaryKey(int index) {
		Column out;
		if(index < primaryKeys.length && index >= 0)
			out = primaryKeys[index];
		else
			out = null;
		return out;
	}

	public ForeignKey[] getForeignKeys() {
		return foreignKeys;
	}

	public ForeignKey getForeignKey(int index) {
		ForeignKey out;
		if(index < foreignKeys.length && index >= 0)
			out = foreignKeys[index];
		else
			out = null;
		return out;
	}

//	Setter
	public void setName(String name) {
		this.name = name;
	}

	public void setColumns(Column[] columns) {
		this.columns = columns;
	}

	public void setPrimaryKeys(Column[] primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	public void setForeignKeys(ForeignKey[] foreignKeys) {
		this.foreignKeys = foreignKeys;
	}

//	toString


	@Override
	public String toString() {
		// Created StringBuilder & added Table name
		StringBuilder out = new StringBuilder("Table: ");
		out.append(name + "\n----------------------------------------------");

		// Added Columns
		out.append("\nColumns:\t" +
				"\t");
		for(Column c : columns) {
			out.append(c + " ");
		}

		// Added Primary Keys
		out.append("\nPrimary Keys:\t");
		for(Column c : primaryKeys) {
			out.append(c + " ");
		}

		// Added Foreign Keys
		out.append("\nForeign Keys:\t");
		for(ForeignKey k : foreignKeys) {
			out.append(k + " ");
		}
		out.append('\n');

		return out.toString();
	}

	//	Equals
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Table table = (Table) o;
		return name.equals(table.name) && Arrays.equals(columns, table.columns) && Arrays.equals(primaryKeys, table.primaryKeys) && Arrays.equals(foreignKeys, table.foreignKeys);
	}

//	HashCode
	@Override
	public int hashCode() {
		int result = Objects.hash(name);
		result = 31 * result + Arrays.hashCode(columns);
		result = 31 * result + Arrays.hashCode(primaryKeys);
		result = 31 * result + Arrays.hashCode(foreignKeys);
		return result;
	}
}
