package database;

import java.util.Objects;

public class Column {
	private String name;
	private String dataType;

//	Constructor
	public Column(String name, String dataType) {
		this.name = name;
		this.dataType = dataType;
	}

//	Getter
	public String getName() {
		return name;
	}

	public String getDataType() {
		return dataType;
	}

//	Setter
	public void setName(String name) {
		this.name = name;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

//	Equals
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Column column = (Column) o;
		return Objects.equals(name, column.name) && Objects.equals(dataType, column.dataType);
	}

//	ToString
	@Override
	public String toString() {
		return "[" + name + " (" + dataType + ")]";
	}
}
