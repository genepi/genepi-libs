package genepi.io.text;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;

public class LineReader extends AbstractLineReader<String> {

	private String line;

	public LineReader(String filename) throws IOException {
		super(filename);
	}

	public LineReader(DataInputStream inputStream) throws IOException {
		super(inputStream);
	}

	@Override
	public String get() {
		return line;
	}

	@Override
	public Iterator<String> iterator() {
		return null;
	}

	@Override
	protected void parseLine(String line) throws IOException {
		this.line = line;
	}

}
