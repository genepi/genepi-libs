package genepi.io.mach;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MachHapsWriter {

	private BufferedWriter bw;

	private boolean first = true;

	public MachHapsWriter(String filename) throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		first = true;
	}

	public void write(MachHap hap) throws IOException {
		if (first) {
			first = false;
		} else {
			bw.newLine();
		}

		StringBuilder haps = new StringBuilder();
		for (int i : hap.getHaps()) {
			haps.append(i);
		}

		bw.write(hap.getSample() + " HAPLO " + haps);

	}

	public void close() throws IOException{
		bw.close();
	}

}
