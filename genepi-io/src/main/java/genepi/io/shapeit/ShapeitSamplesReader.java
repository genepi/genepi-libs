package genepi.io.shapeit;

import genepi.io.FileUtil;
import genepi.io.reader.IReader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public class ShapeitSamplesReader implements IReader<ShapeitSample> {

	private String filename;

	private BufferedReader in;

	private String line;

	private ShapeitSample currentSnp;

	public ShapeitSamplesReader(String filename) throws IOException {
		this.filename = filename;

		InputStream in2 = FileUtil.decompressStream(new FileInputStream(
				filename));
		in = new BufferedReader(new InputStreamReader(in2));

		line = in.readLine();
		String[] tiles = line.split("\\s{1}(?!\\s)");
		if (tiles.length < 3) {
			throw new IOException("Wrong Header.");
		}

		if (!tiles[0].trim().equals("ID_1") || !tiles[1].trim().equals("ID_2")
				|| !tiles[2].trim().equals("missing")) {
			throw new IOException("Wrong Header.");
		}
		line = in.readLine();
		tiles = line.split("\\s{1}(?!\\s)");

		if (tiles.length < 3) {
			throw new IOException("Wrong Header.");
		}

		if (!tiles[0].trim().equals("0") || !tiles[1].trim().equals("0")
				|| !tiles[2].trim().equals("0")) {
			throw new IOException("Wrong Header.");
		}

	}

	@Override
	public boolean next() throws IOException {
		line = in.readLine();
		if (line != null) {
			currentSnp = new ShapeitSample(line);
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
	public ShapeitSample get() {
		return currentSnp;
	}

	@Override
	public Iterator<ShapeitSample> iterator() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void reset() {

	}

	public String getFilename() {
		return filename;
	}

}
