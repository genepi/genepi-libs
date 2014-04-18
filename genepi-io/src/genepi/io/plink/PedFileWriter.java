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

			switch (individual.getAlleles()[i]) {
			case 0:
				bw.write(" 0");
				break;
			case 1:
				bw.write(" A");
				break;
			case 2:
				bw.write(" C");
				break;
			case 3:
				bw.write(" G");
				break;
			case 4:
				bw.write(" T");
				break;
			}

		}

	}

	public void close() throws IOException {
		bw.close();
	}

}
