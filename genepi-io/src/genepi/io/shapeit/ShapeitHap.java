package genepi.io.shapeit;

import java.io.IOException;

public class ShapeitHap {

	private String chromosome;

	private String snp;

	private long position;

	private String firstAllele;

	private String secondAllele;

	private int haps[];

	public ShapeitHap(String line) throws IOException {
		String[] tiles = line.split("\\s{1}(?!\\s)");
		if (tiles.length > 5) {
			chromosome = tiles[0].trim();
			/*if (!Chromosome.isValid(chromosome)) {
				throw new IOException("Invalid Chromosome " + chromosome + ".");
			}*/
			snp = tiles[1].trim();
			position = Long.parseLong(tiles[2].trim());
			firstAllele = tiles[3].trim();
			secondAllele = tiles[4].trim();

			if (!firstAllele.equals("N") && !firstAllele.equals("A")
					&& !firstAllele.equals("C") && !firstAllele.equals("G")
					&& !firstAllele.equals("T") && !firstAllele.equals("0")
					&& !firstAllele.equals("1") && !firstAllele.equals("2")
					&& !firstAllele.equals("3") && !firstAllele.equals("4")) {
				throw new IOException("First allele of snp " + snp
						+ " is invalid (" + firstAllele + ")");
			}

			if (!secondAllele.equals("N") && !secondAllele.equals("A")
					&& !secondAllele.equals("C") && !secondAllele.equals("G")
					&& !secondAllele.equals("T") && !secondAllele.equals("0")
					&& !secondAllele.equals("1") && !secondAllele.equals("2")
					&& !secondAllele.equals("3") && !secondAllele.equals("4")) {
				throw new IOException("Second allele of snp " + snp
						+ " is invalid (" + secondAllele + ")");
			}

			haps = new int[tiles.length - 5];
			for (int i = 0; i < haps.length; i++) {
				haps[i] = Integer.parseInt(tiles[i + 5].trim());
				if (haps[i] != 0 && haps[i] != 1) {
					throw new IOException("Haplotypes for snp " + snp
							+ " have invalid syntax.");
				}
			}

		} else {
			throw new IOException("Expected: 5 columns. Found: " + tiles.length
					+ " columns.");
		}
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

	public String getFirstAllele() {
		return firstAllele;
	}

	public void setFirstAllele(String firstAllele) {
		this.firstAllele = firstAllele;
	}

	public String getSecondAllele() {
		return secondAllele;
	}

	public void setSecondAllele(String secondAllele) {
		this.secondAllele = secondAllele;
	}

	public String getSnp() {
		return snp;
	}

	public void setSnp(String snp) {
		this.snp = snp;
	}

	public String toString() {
		return snp + " (" + getNumberHaps() + " haps)";
	}

}
