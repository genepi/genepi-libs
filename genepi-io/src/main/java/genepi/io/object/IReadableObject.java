package genepi.io.object;

import java.io.DataInput;
import java.io.IOException;

public interface IReadableObject {

	public void read(DataInput in) throws IOException;

}
