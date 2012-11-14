package genepi.io.text;


import java.io.IOException;
import java.util.Iterator;

public class TextReaderIterator<o> implements Iterator<o> {

	private ITextReader<o> reader;

	public TextReaderIterator(ITextReader<o> reader) {
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
