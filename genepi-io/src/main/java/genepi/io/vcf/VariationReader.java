package genepi.io.vcf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import genepi.io.reader.IReader;

public class VariationReader implements IReader<Variation> {

	private String filename;

	private BufferedReader in;

	private String line;

	private Variation currentVariation;

	public VariationReader(String filename) {
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
		
		while(line != null && line.startsWith("#")){
			line = in.readLine();
		}
		
		if (line != null) {
			currentVariation = new Variation(line);
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
	public Variation get() {
		return currentVariation;
	}

	@Override
	public Iterator<Variation> iterator() {
		return null;
	}

	@Override
	public void reset() {

	}

	public String getFilename() {
		return filename;
	}

}
