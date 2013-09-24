package genepi.io.plink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PedFileWriter {

	private BufferedWriter bw;

	private boolean first = true;

	public PedFileWriter(String filename) throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		first = true;
	}

	public void write(Sample individual) throws IOException {
		if (first) {
			first = false;
		} else {
			bw.newLine();
		}

		bw.write(individual.getFamily() + " " + individual.getId() + " "
				+ individual.getFather() + " " + individual.getMother() + " "
				+ individual.getSex() + " " + individual.getPhenotype());

		for (int i = 0; i < individual.getAlleles().length; i++) {
			bw.write(" " + individual.getAlleles()[i]);
		}

	}

	public void close() throws IOException {
		bw.close();
	}

}
