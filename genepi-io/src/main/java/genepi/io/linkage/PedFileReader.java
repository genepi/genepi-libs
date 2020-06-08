package genepi.io.linkage;

import genepi.io.text.AbstractLineReader;

import java.io.IOException;
import java.util.Iterator;

public class PedFileReader extends AbstractLineReader<Sample> {

	private Sample currentIndividual;

	public PedFileReader(String filename) throws IOException {
		super(filename);
	}

	protected void parseLine(String line) throws Exception {
		currentIndividual = new Sample(line);
	}

	@Override
	public Sample get() {
		return currentIndividual;
	}

	@Override
	public Iterator<Sample> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
