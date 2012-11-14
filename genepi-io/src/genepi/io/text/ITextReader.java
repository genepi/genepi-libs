package genepi.io.text;

import java.io.IOException;

public interface ITextReader<o> extends Iterable<o> {

	public boolean next() throws IOException;

	public void reset();

	public o get();

	public void close() throws IOException;

}
