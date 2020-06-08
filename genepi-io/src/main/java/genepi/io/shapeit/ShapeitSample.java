package genepi.io.shapeit;

import java.io.IOException;

public class ShapeitSample {

	private String id1;

	private String id2;

	public ShapeitSample(String id1, String id2) {
		this.id1 = id1;
		this.id2 = id2;
	}

	public ShapeitSample(String line) throws IOException {
		String[] tiles = line.split("\\s{1}(?!\\s)");
		if (tiles.length >= 3) {
			id1 = tiles[0].trim();
			id2 = tiles[1].trim();
		} else {
			throw new IOException("Parsing error.");
		}
	}

	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getId2() {
		return id2;
	}

	public void setId2(String id2) {
		this.id2 = id2;
	}

	public String toString() {
		return id1 + "(" + id2 + ")";
	}

}
