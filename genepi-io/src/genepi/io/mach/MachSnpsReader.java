package genepi.io.mach;

import genepi.io.text.AbstractLineReader;

import java.io.IOException;
import java.util.Iterator;

public class MachSnpsReader extends AbstractLineReader<MachSnp> {

	private MachSnp currentSnp;

	public MachSnpsReader(String filename) throws IOException {
		super(filename);
	}

	protected void parseLine(String line) throws Exception {
		currentSnp = new MachSnp(line);
	}

	@Override
	public MachSnp get() {
		return currentSnp;
	}

	@Override
	public Iterator<MachSnp> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
