package genepi.io.vcf;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import genepi.io.reader.IReader;

public class SnpAliasReader implements IReader<SnpAlias> {

	private String filename;

	private BufferedReader in;

	private String line;

	private SnpAlias currentSnpAlias;

	public SnpAliasReader(String filename) {
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
			if (!line.trim().isEmpty()) {
				try {
					currentSnpAlias = new SnpAlias(line);
				} catch (Exception e) {
					System.out.println(line);
					return next();
				}
			} else {
				return false;
			}
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
	public SnpAlias get() {
		return currentSnpAlias;
	}

	@Override
	public Iterator<SnpAlias> iterator() {
		return null;
	}

	@Override
	public void reset() {

	}

	public String getFilename() {
		return filename;
	}

}
