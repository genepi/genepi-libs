package genepi.io.vcf;

public class Variation {

	private String rsNumber;

	private long position;

	private String chromosome;

	public Variation(String line) {
		String tiles[] = line.split("[\t]", 4);
		chromosome = tiles[0];
		position = Long.parseLong(tiles[1]);
		rsNumber = tiles[2];
	}

	public String getRsNumber() {
		return rsNumber;
	}

	public void setRsNumber(String rsNumber) {
		this.rsNumber = rsNumber;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

}
