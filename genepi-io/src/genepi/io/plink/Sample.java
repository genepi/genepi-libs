package genepi.io.plink;

import java.io.IOException;

public class Sample {

	private String id;

	private byte[] alleles;

	private String family;

	private String father;

	private String mother;

	private int sex;

	private float phenotype;

	private boolean noPhenotype = false;

	public Sample(String line) throws IOException {

		System.out.println(line);
		
		// parse line
		String[] tiles = line.split("\\s{1}(?!\\s)");

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

		int offset = 0;

		// phenotype in column 5?
		if ((tiles.length - 6) % 2 == 0) {
			offset = 6;
			noPhenotype = false;
		} else {
			offset = 5;
			noPhenotype = true;
		}

		String firstAllele = tiles[offset];
		
		boolean separated = (firstAllele.length() == 1);
		if (separated){
			alleles = new byte[tiles.length - offset];
		}else{
			alleles = new byte[2 * (tiles.length - offset)+2];
		}

		int count = 0;
		for (int i = 0; i < tiles.length - offset; i++) {

			char allele = tiles[i + offset].charAt(0);
		
			alleles[count] = 0;

			switch (allele) {
			case 'N':
			case 'n':
			case '0':
			case 'i':
			case 'I':
			case 'd':
			case 'D':				
				alleles[count] = 0;
				break;
			case 'A':
			case 'a':
			case '1':
				alleles[count] = 1;
				break;
			case 'C':
			case 'c':
			case '2':
				alleles[count] = 2;
				break;
			case 'G':
			case 'g':
			case '3':
				alleles[count] = 3;
				break;
			case 'T':
			case 't':
			case '4':
				alleles[count] = 4;
				break;
			default:
				throw new IOException("Genotype coding is invalid: " + allele
						+ ".");
			}
			count++;
			
			if (!separated){
				char alleleB = tiles[i + offset].charAt(2);
				alleles[count] = 0;

				switch (alleleB) {
				case 'N':
				case 'n':
				case '0':
				case 'i':
				case 'I':
				case 'd':
				case 'D':						
					alleles[count] = 0;
					break;
				case 'A':
				case 'a':
				case '1':
					alleles[count] = 1;
					break;
				case 'C':
				case 'c':
				case '2':
					alleles[count] = 2;
					break;
				case 'G':
				case 'g':
				case '3':
					alleles[count] = 3;
					break;
				case 'T':
				case 't':
				case '4':
					alleles[count] = 4;
					break;
				default:
					throw new IOException("Genotype coding is invalid: " + allele
							+ ".");
				}
				count++;
			}

		}

	}

	public Sample(Sample sample, byte[] alleles) {
		family = sample.family;
		id = sample.id;
		father = sample.father;
		mother = sample.mother;
		sex = sample.sex;
		phenotype = sample.phenotype;
		this.alleles = alleles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getAlleles() {
		return alleles;
	}

	public void setAlleles(byte[] alleles) {
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

	public float getPhenotype() {
		return phenotype;
	}

	public void setPhenotype(float phenotype) {
		this.phenotype = phenotype;
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

	public boolean hasNoPhenotype() {
		return noPhenotype;
	}

}
