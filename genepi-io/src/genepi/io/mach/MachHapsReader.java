package genepi.io.mach;

import genepi.io.text.AbstractLineReader;

import java.io.IOException;
import java.util.Iterator;

public class MachHapsReader extends AbstractLineReader<MachHap> {

	private MachHap currentHap;

	public MachHapsReader(String filename) throws IOException {
		super(filename);
	}

	protected void parseLine(String line) throws IOException {
		currentHap = new MachHap(line);
	}

	@Override
	public MachHap get() {
		return currentHap;
	}

	@Override
	public Iterator<MachHap> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

}
