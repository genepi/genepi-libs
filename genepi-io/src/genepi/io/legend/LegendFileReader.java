package genepi.io.legend;

import genepi.io.FileUtil;
import genepi.io.text.AbstractLineReader;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LegendFileReader extends AbstractLineReader<String> {

	private Map<Integer, Integer> index = new HashMap<Integer, Integer>();

	private String line;

	private int oldOffset = 0;

	private BufferedReader myIn;

	private LegendEntry entry = new LegendEntry();

	private String population;

	public LegendFileReader(DataInputStream inputStream) throws IOException {
		super(inputStream);
	}

	public LegendFileReader(String filename, String population)
			throws IOException {
		super(filename);
		this.population = population;
	}

	public void createIndex() throws IOException {

		int offset = 0;

		while (next()) {
			String line = get();
			if (!line.startsWith("id")) {
				String[] tiles = line.split(" ", 3);
				int position = Integer.parseInt(tiles[1]);
				index.put(position, offset);
			}
			offset += line.length() + 1;
		}
		close();
	}

	@Override
	public String get() {
		return line;
	}

	public String findByPosition(int position) throws IOException {

		Integer offset = index.get(position);
		if (offset != null) {

			FileInputStream inputStream = new FileInputStream(getFilename());
			InputStream in2 = FileUtil.decompressStream(inputStream);
			BufferedReader in = new BufferedReader(new InputStreamReader(in2));
			in.skip(offset);
			String line = in.readLine();
			in.close();
			return line;

		} else {

			return null;

		}
	}

	public void initSearch() throws IOException {
		FileInputStream inputStream = new FileInputStream(getFilename());
		InputStream in2 = FileUtil.decompressStream(inputStream);
		myIn = new BufferedReader(new InputStreamReader(in2));
	}

	public LegendEntry findByPosition2(int position) throws IOException {

		Integer offset = index.get(position);
		if (offset != null) {

			myIn.skip(offset - oldOffset);
			String line = myIn.readLine();
			oldOffset = offset + line.length() + 1;

			String[] tiles = line.split(" ");

			entry.setRsId(tiles[0]);
			entry.setAlleleA(tiles[2].charAt(0));
			entry.setAlleleB(tiles[3].charAt(0));
			entry.setType(tiles[4]);

			float aaf = 0;

			if (population.equals("afr")) {
				aaf = Float.parseFloat(tiles[7]);
			}

			if (population.equals("amr")) {
				aaf = Float.parseFloat(tiles[8]);
			}

			if (population.equals("asn")) {
				aaf = Float.parseFloat(tiles[9]);
			}

			if (population.equals("eur")) {
				aaf = Float.parseFloat(tiles[10]);
			}

			entry.setFrequencyA(1 - aaf);
			entry.setFrequencyB(aaf);

			return entry;

		} else {

			return null;

		}
	}

	@Override
	public Iterator<String> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void parseLine(String arg0) throws Exception {
		line = arg0;
	}

}
