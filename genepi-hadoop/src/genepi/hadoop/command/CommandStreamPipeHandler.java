package genepi.hadoop.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommandStreamPipeHandler implements Runnable {

	private InputStream is;

	private OutputStream out;

	//private StringBuffer stdout = new StringBuffer();;

	public CommandStreamPipeHandler(InputStream is, OutputStream out) {
		this.is = is;
		this.out = out;
	}

	@Override
	public void run() {

		try {

			byte[] buffer = new byte[200];

			int size = 0;

			while ((size = is.read(buffer)) > 0) {
				String line = new String(buffer, 0, size);
				//stdout.append(line);

				out.write(buffer, 0, size);

			}

			out.close();

			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
