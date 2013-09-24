package genepi.io.plink;

import java.io.IOException;

public class Snp {

	private int chromosome;

	private String id;

	private float geneticPosition;

	private int physicalPosition;

	public Snp(String line) throws IOException {
		// parse line
		String[] tiles = line.split("\\s{1}(?!\\s)");

		if (tiles.length == 4) {

			chromosome = Integer.parseInt(tiles[0]);
			id = tiles[1];
			geneticPosition = Float.parseFloat(tiles[2]);
			physicalPosition = Integer.parseInt(tiles[3]);

		} else {

			throw new IOException("Parsing error.");

		}

	}
	
	public Snp() throws IOException {
		
	}

	public int getChromosome() {
		return chromosome;
	}

	public void setChromosome(int chromosome) {
		this.chromosome = chromosome;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getGeneticPosition() {
		return geneticPosition;
	}

	public void setGeneticPosition(float geneticPosition) {
		this.geneticPosition = geneticPosition;
	}

	public int getPhysicalPosition() {
		return physicalPosition;
	}

	public void setPhysicalPosition(int physicalPosition) {
		this.physicalPosition = physicalPosition;
	}

}
