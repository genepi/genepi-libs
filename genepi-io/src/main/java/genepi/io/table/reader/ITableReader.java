package genepi.io.table.reader;

import java.io.IOException;

public interface ITableReader extends Iterable<Row>{

	public String[] getColumns();

	public boolean next();

	public double getDouble(String column);

	public double getDouble(int column);

	public String getString(String column) throws IOException;

	public int getInteger(String column);

	public String getString(int column);

	public int getInteger(int column);

	public int getColumnIndex(String column);

	public String[] getRow();
	
	public Row getAsObject();

	public void close();

	boolean hasColumn(String column);

}
