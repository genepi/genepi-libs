package genepi.io.shapeit;

public class Chromosome {

	public static boolean isValid(String chromosome) {

		return (chromosome.equals("1") || chromosome.equals("2")
				|| chromosome.equals("3") || chromosome.equals("4")
				|| chromosome.equals("6") || chromosome.equals("5")
				|| chromosome.equals("7") || chromosome.equals("8")
				|| chromosome.equals("9") || chromosome.equals("10")
				|| chromosome.equals("11") || chromosome.equals("12")
				|| chromosome.equals("13") || chromosome.equals("14")
				|| chromosome.equals("15") || chromosome.equals("16")
				|| chromosome.equals("17") || chromosome.equals("18")
				|| chromosome.equals("19") || chromosome.equals("20")
				|| chromosome.equals("21") || chromosome.equals("22")
				|| chromosome.equals("23") || chromosome.equals("24")
				|| chromosome.equals("X") || chromosome.equals("Y") || chromosome
					.equals("MT"));
	}

}
