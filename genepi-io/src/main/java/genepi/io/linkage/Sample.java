package genepi.io.linkage;

import java.io.IOException;

public class Sample {

	private String id;

	private char[] alleles;

	private String family;

	private String father;

	private String mother;

	private int sex;

	public Sample(String line) throws IOException {

		// parse line
		String[] tiles = line.split("\\s{1}(?!\\s)|(/)");

		if (tiles.length < 6) {
			throw new IOException("Expected: at last 6 columns. Found: "
					+ tiles.length + " columns.");
		}

		family = tiles[0];
		id = tiles[1];
		father = tiles[2];
		mother = tiles[3];
		try {
			sex = Integer.parseInt(tiles[4]);
		} catch (Exception e) {
			throw new IOException("Sex is not an integer: " + tiles[4] + ".");
		}

		alleles = new char[tiles.length - 5];
		for (int i = 0; i < tiles.length - 5; i++) {
			if (tiles[i + 5].length() == 1) {
				alleles[i] = tiles[i + 5].charAt(0);
			} else {
				throw new IOException("Genotype coding is invalid: "
						+ tiles[i + 5] + ".");
			}
		}

	}

	public Sample(Sample sample, char[] alleles) {

		family = sample.family;
		id = sample.id;
		father = sample.father;
		mother = sample.mother;
		sex = sample.sex;
		this.alleles = alleles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public char[] getAlleles() {
		return alleles;
	}

	public void setAlleles(char[] alleles) {
		this.alleles = alleles;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String toString() {
		return id + " (" + family + ") " + sex + " " + alleles.length / 2
				+ " snps " + (alleles[alleles.length - 1]);
	}

	public int getNoAlleles() {
		return alleles.length;
	}

	public int getNoSnps() {
		return alleles.length / 2;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new Sample(
				"1000-10D 1000-10D 0 0 0 4/4 2/2 2/2 1/1 4/4 3/3 2/2 4/4 2/2 4/4 1/1 1/1 2/2 2/2 2/2 2/2 2/2 3/3 4/4 1/1 2/2 2/2 1/1 1/3 3/3 3/3 4/4 3/3 2/2 1/1 4/4 1/1 4/4 2/2 2/2 3/3 4/4 2/2 3/3 4/4 1/1 2/2 3/3 3/3 2/2 4/4 3/3 2/2 2/2 1/1 3/3 1/1 3/3 2/2 1/1 1/1 3/3 2/2 3/3 4/4 4/4 4/4 3/3 3/3 4/4 2/2 3/3 3/3 4/4 2/2 2/2 3/3 2/2 2/2 3/3 1/1 2/2 3/1 2/2 4/4 2/2 3/3 4/4 1/1 4/4 4/4 1/2 2/2 3/3 3/3 4/4 1/1 2/2 2/2 2/2 1/1 4/4 4/4 4/4 3/3 2/2 3/3 2/2 3/3 2/2 3/3 2/2 2/2 3/3 2/4 4/4 2/2 3/3 3/3 1/3 4/4 3/3 3/3 1/1 2/2 2/2 2/2 4/2 3/3 1/1 3/3 4/4 1/1 3/3 4/4 3/3 2/2 1/1 1/1 4/4 2/2 1/1 4/4 1/1 4/4 3/3 2/2 3/3 2/2 1/1 2/2 2/2 1/1 4/4 2/4 1/1 2/2 4/1 3/2 3/3 2/2 1/1 1/1 2/2 2/2 2/2 1/1 3/3 1/1 3/3 4/4 2/2 3/3 3/3 1/1 2/1 2/2 4/4 1/1 2/2 1/2 1/1 1/1 2/2 4/4 3/3 2/1 3/3 4/4 3/1 4/4 3/3 2/2 2/2 1/3 1/1 2/4 2/2 2/2 2/2 3/3 2/2 2/2 3/3 1/1 3/3 2/2 4/4 4/4 1/1 4/4 3/1 4/4 4/4 1/1 4/4 1/1 3/3 1/3 2/2 4/4 4/4 4/4 4/4 3/3 1/1 4/3 4/4 4/4 4/4 1/1 2/2 2/2 2/2 2/2 3/1 2/2 4/4 4/2 1/1 3/1 3/3 4/4 1/1 4/4 4/4 4/4 1/1 2/2 2/2 1/1 4/4 1/3 3/3 3/3 2/2 4/4 2/2 3/3 1/1 2/2 2/2 3/3 4/4 2/2 4/2 2/2 1/1 4/4 4/4 4/4 3/1 1/1 3/3 4/4 3/3 2/4 1/1 3/3 1/1 2/2 2/2 1/1 1/3 4/4 2/2 4/1 2/2 3/3 1/1 4/4 4/4 3/3 4/4 4/2 1/1 2/2 1/1 1/1 1/1 2/2 1/1 1/3 1/1 2/2 2/2 3/3 1/1 2/2 4/4 1/1 4/4 4/4 4/4 2/2 4/4 2/2 3/3 2/2 3/3 1/1 1/1 3/4 2/2 1/1 3/3 1/1 4/4 1/1 3/3 1/1 4/4 2/2 3/3 1/1 2/2 3/3 1/1 1/1 2/3 3/1 3/3 2/2 1/1 3/1 3/3 3/3 1/1 3/3 3/3 1/1");

	}

}
