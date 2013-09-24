package genepi.io.shapeit;

import genepi.io.text.AbstractLineReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;

public class ShapeitHapsReader extends AbstractLineReader<ShapeitHap> {

	private ShapeitHap currentHap;

	public ShapeitHapsReader(String filename) throws IOException {
		super(filename);
	}

	public ShapeitHapsReader(DataInputStream inputStream) throws IOException {
		super(inputStream);
	}

	protected void parseLine(String line) throws Exception {
		currentHap = new ShapeitHap(line);
	}

	@Override
	public ShapeitHap get() {
		return currentHap;
	}

	@Override
	public Iterator<ShapeitHap> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
