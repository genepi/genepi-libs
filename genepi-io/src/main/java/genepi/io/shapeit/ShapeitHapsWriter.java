package genepi.io.shapeit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ShapeitHapsWriter {

	private BufferedWriter bw;

	private boolean first = true;

	public ShapeitHapsWriter(String filename) throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		first = true;
	}

	public void write(ShapeitHap hap) throws IOException {
		if (first) {
			first = false;
		} else {
			bw.newLine();
		}

		StringBuilder haps = new StringBuilder();
		for (int i : hap.getHaps()) {
			haps.append(" ");
			haps.append(i);
		}

		bw.write(hap.getChromosome() + " " + hap.getSnp() + " "
				+ hap.getPosition() + " " + hap.getFirstAllele() + " "
				+ hap.getSecondAllele() + haps);

	}

	public void close() throws IOException {
		bw.close();
	}

}
