package genepi.io.table.reader;

public class Row {

	private String[] line;

	private ITableReader reader;

	public Row(ITableReader reader) {
		line = reader.getRow();
		this.reader = reader;
	}

	public int getInteger(String column) {
		return getInteger(reader.getColumnIndex(column));
	}

	public String getString(String column) {
		return getString(reader.getColumnIndex(column));
	}

	public double getDouble(String column) {
		return getDouble(reader.getColumnIndex(column));
	}

	public double getDouble(int column) {
		return Double.parseDouble(line[column]);
	}

	public int getInteger(int column) {
		return Integer.parseInt(line[column]);
	}

	public String getString(int column) {
		return line[column];
	}

}
