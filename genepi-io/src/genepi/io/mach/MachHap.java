package genepi.io.mach;

import java.io.IOException;

public class MachHap {

	private String sample;

	private int haps[];

	public MachHap(String line) throws IOException {
		String[] tiles = line.split(" ");
		if (tiles.length == 3) {
			sample = tiles[0];
			String type = tiles[1];
			if (type.equals("HAPLO")) {
				haps = new int[tiles[2].length()];
				for (int i = 0; i < tiles[2].length(); i++) {
					haps[i] = Integer.parseInt(tiles[2].charAt(i) + "");
				}
			} else {
				throw new IOException("Parsing error.");
			}
		} else {
			throw new IOException("Parsing error.");
		}
	}

	public MachHap(String sample, int haps[]) {
		this.sample = sample;
		this.haps = haps;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public int[] getHaps() {
		return haps;
	}

	public void setHaps(int[] haps) {
		this.haps = haps;
	}

	public int getNumberHaps() {
		return haps.length;
	}

	public String toString() {
		return sample + " (" + getNumberHaps() + " haps)";
	}

}
