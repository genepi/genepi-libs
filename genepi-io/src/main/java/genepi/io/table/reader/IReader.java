package genepi.io.table.reader;

import java.io.IOException;

public interface IReader<o> extends Iterable<o> {

	public boolean next() throws IOException;

	public void reset();

	public o get();

	public void close() throws IOException;

}
