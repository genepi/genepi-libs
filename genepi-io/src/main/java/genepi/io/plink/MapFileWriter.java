package genepi.io.plink;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapFileWriter {

	private BufferedWriter bw;

	private boolean first = true;

	public MapFileWriter(String filename) throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		first = true;
	}

	public void write(Snp snp) throws IOException {
		if (first) {
			first = false;
		} else {
			bw.newLine();
		}
		bw.write(snp.getChromosome() + " " + snp.getId() + " "
				+ snp.getGeneticPosition() + " " + snp.getPhysicalPosition());
	}

	public void close() throws IOException {
		bw.close();
	}

}
