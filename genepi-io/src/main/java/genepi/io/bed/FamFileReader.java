package genepi.io.bed;

import genepi.io.text.AbstractLineReader;

import java.io.IOException;
import java.util.Iterator;

public class FamFileReader extends AbstractLineReader<String> {

	private String currentSample;

	public FamFileReader(String filename) throws IOException {
		super(filename);
	}

	protected void parseLine(String line) throws Exception {
		currentSample = line;
	}

	@Override
	public String get() {
		return currentSample;
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
