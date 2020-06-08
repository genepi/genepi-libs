package genepi.io.table.writer;

import java.io.IOException;

public interface IWriter<o> {
	
	public void write(o object) throws IOException;

	public void close() throws IOException;
	
}
