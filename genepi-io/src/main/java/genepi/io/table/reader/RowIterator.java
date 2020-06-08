package genepi.io.table.reader;

import java.util.Iterator;

public class RowIterator implements Iterator<Row> {

	private ITableReader reader;

	public RowIterator(ITableReader reader) {
		this.reader = reader;
	}

	@Override
	public boolean hasNext() {
		return reader.next();
	}

	@Override
	public Row next() {
		return reader.getAsObject();
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
