package genepi.io.table.reader;

import java.io.IOException;
import java.util.Iterator;

public class ReaderIterator<o> implements Iterator<o> {

	private IReader<o> reader;

	public ReaderIterator(IReader<o> reader) {
		this.reader = reader;
	}

	@Override
	public boolean hasNext() {
		try {
			return reader.next();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public o next() {
		return reader.get();
	}

	@Override
	public void remove() {

	}

}
