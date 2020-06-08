package genepi.io.shapeit;

import genepi.io.FileUtil;
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
		String chr = null;
		String[] tiles =  FileUtil.getFilename(getFilename()).split("\\.");
		for (String tile : tiles) {
			if (tile.toLowerCase().startsWith("chr")) {
				chr = tile.replaceAll("chr", "");
			}
		}

		currentHap = new ShapeitHap(line, chr);
	}

	@Override
	public ShapeitHap get() {
		return currentHap;
	}

	@Override
	public Iterator<ShapeitHap> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public static void main(String[] args) {
		String filename = "metsim.gwas.b37.chr15.sample";
		String chr = null;
		String[] tiles =  FileUtil.getFilename(filename).split("\\.");
		for (String tile : tiles) {
			if (tile.toLowerCase().startsWith("chr")) {
				chr = tile.replaceAll("chr", "");
			}
		}
		System.out.println(chr);
	}
	
}
