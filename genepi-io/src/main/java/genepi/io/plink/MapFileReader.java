package genepi.io.plink;

import genepi.io.text.AbstractLineReader;

import java.io.IOException;
import java.util.Iterator;

public class MapFileReader extends AbstractLineReader<Snp> {

	private Snp currentSnp;

	public MapFileReader(String filename) throws IOException {
		super(filename);
	}

	protected void parseLine(String line) throws Exception {
		currentSnp = new Snp(line);
	}

	@Override
	public Snp get() {
		return currentSnp;
	}

	@Override
	public Iterator<Snp> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
