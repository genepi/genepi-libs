package genepi.io.table.reader;

import java.util.Iterator;

import genepi.io.table.exceptions.ColumnNotFoundException;

public abstract class AbstractTableReader implements ITableReader {

	@Override
	public int getInteger(String column) {
		if (hasColumn(column)) {
			return getInteger(getColumnIndex(column));
		} else {
			throw new ColumnNotFoundException("Column '" + column + "' not available");
		}
	}

	@Override
	public String getString(String column) {
		if (hasColumn(column)) {
			return getString(getColumnIndex(column));
		} else {
			throw new ColumnNotFoundException("Column '" + column + "' not available");
		}
	}

	@Override
	public double getDouble(String column) {
		if (hasColumn(column)) {
			return getDouble(getColumnIndex(column));
		} else {
			throw new ColumnNotFoundException("Column '" + column + "' not available");
		}
	}

	@Override
	public double getDouble(int column) {
		return Double.parseDouble(getRow()[column]);
	}

	@Override
	public int getInteger(int column) {
		return Integer.parseInt(getRow()[column]);
	}

	@Override
	public String getString(int column) {
		return getRow()[column];
	}

	@Override
	public Iterator<Row> iterator() {
		return new RowIterator(this);
	}

	@Override
	public Row getAsObject() {
		return new Row(this);
	}
}
