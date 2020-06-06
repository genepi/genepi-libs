package genepi.io.table.writer;

public abstract class AbstractTableWriter implements ITableWriter {

	@Override
	public void setInteger(String column, int value) {
		setInteger(getColumnIndex(column), value);

	}

	@Override
	public void setDouble(String column, double value) {
		setDouble(getColumnIndex(column), value);

	}

	@Override
	public void setString(String column, String value) {
		setString(getColumnIndex(column), value);
	}
}
