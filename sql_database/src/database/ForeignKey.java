package database;

public class ForeignKey extends Column{
	private Table referencedTable;
	private Column referencedColumn;
	private String constraintName;

//	Constructors
	public ForeignKey(String name, String dataType, Table referencedTable, Column referencedColumn, String constraintName) {
		super(name, dataType);
		this.referencedTable = referencedTable;
		this.referencedColumn = referencedColumn;
		this.constraintName = constraintName;
	}

	public ForeignKey(String name, String dataType, Table referencedTable, Column referencedColumn) {
		super(name, dataType);
		this.referencedTable = referencedTable;
		this.referencedColumn = referencedColumn;
	}

//	Getter
	public Table getReferencedTable() {
		return referencedTable;
	}

	public Column getReferencedColumn() {
		return referencedColumn;
	}

	public String getConstraintName() {
		return constraintName;
	}

//	ToString

	@Override
	public String toString() {
		return "[" + super.getName() + ", " + constraintName + ", " + referencedTable.getName() + "(" + referencedColumn.getName() + ")]";
	}
}
