package genepi.io.linkage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DatFileWriter {

	private BufferedWriter bw;

	private boolean first = true;

	public DatFileWriter(String filename) throws IOException {
		bw = new BufferedWriter(new FileWriter(new File(filename), false));
		first = true;
	}

	public void write(Marker snp) throws IOException {
		if (first) {
			first = false;
		} else {
			bw.newLine();
		}
		bw.write("M " + snp.getId());
	}

	public void close() throws IOException {
		bw.close();
	}

}
