package genepi.io.mach;

import java.io.IOException;

public class MachSnp {

	private String chromosome;

	private long position;

	private String id = null;

	public MachSnp(String chromosome, long position) {
		this.chromosome = chromosome;
		this.position = position;
	}

	public MachSnp(String line) throws IOException {
		String[] tiles = line.split(":");
		if (tiles.length == 2) {
			chromosome = tiles[0];
			position = Long.parseLong(tiles[1]);
		} else if (tiles.length == 1) {

			if (line.startsWith("rs")) {

				id = line;

			} else {

				throw new IOException("Parsing error.");

			}
		} else {

			throw new IOException("Parsing error.");

		}
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public boolean hasId() {
		return id != null;
	}

	public String toString() {
		return chromosome + ":" + position;
	}

}
