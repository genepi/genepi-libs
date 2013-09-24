package genepi.io.mach;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MachSnpsWriter {

	private BufferedWriter bw;

	private boolean first = true;

	public MachSnpsWriter(String filename) throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		first = true;
	}

	public void write(MachSnp snp) throws IOException {
		if (first) {
			first = false;
		} else {
			bw.newLine();
		}

		bw.write(snp.getChromosome() + ":" + snp.getPosition());
	}

	public void close() throws IOException {
		bw.close();
	}

}
