package genepi.io.vcf;

import genepi.io.table.reader.IReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class GenomeLocationReader implements IReader<GenomeLocation> {

	private String filename;

	private BufferedReader in;

	private String line;

	private GenomeLocation currentGenomeLocation;

	public GenomeLocationReader(String filename) throws IOException {
		this.filename = filename;
		try {
			in = new BufferedReader(new InputStreamReader(new GZIPInputStream(
					new FileInputStream(filename))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean next() throws IOException {
		line = in.readLine();

		while (line != null && line.startsWith("#")) {
			line = in.readLine();
		}

		if (line != null) {
			currentGenomeLocation = new GenomeLocation();
			String[] tiles = line.split("\t");
			String rsNumber = tiles[0];
			String chromosome = tiles[1];
			long position = Long.parseLong(tiles[2]);
			currentGenomeLocation.setId(rsNumber);
			currentGenomeLocation.setChromosome(chromosome);
			currentGenomeLocation.setPosition(position);
		}
		return line != null;
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public GenomeLocation get() {
		return currentGenomeLocation;
	}

	@Override
	public Iterator<GenomeLocation> iterator() {
		return null;
	}

	@Override
	public void reset() {

	}

	public String getFilename() {
		return filename;
	}

}
