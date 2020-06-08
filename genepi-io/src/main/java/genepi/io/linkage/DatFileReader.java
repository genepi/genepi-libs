package genepi.io.linkage;

import genepi.io.text.AbstractLineReader;

import java.io.IOException;
import java.util.Iterator;

public class DatFileReader extends AbstractLineReader<Marker> {

	private Marker currentSnp;

	public DatFileReader(String filename) throws IOException {
		super(filename);
	}

	protected void parseLine(String line) throws Exception {
		currentSnp = new Marker(line);
	}

	@Override
	public Marker get() {
		return currentSnp;
	}

	@Override
	public Iterator<Marker> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
