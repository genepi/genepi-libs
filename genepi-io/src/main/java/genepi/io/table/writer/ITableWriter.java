package genepi.io.table.writer;

public interface ITableWriter {

	public void setColumns(String[] columns);

	public void setInteger(int column, int value);

	public void setInteger(String column, int value);

	public void setDouble(int column, double value);

	public void setDouble(String column, double value);

	public void setString(int column, String value);

	public void setString(String column, String value);

	public int getColumnIndex(String column);
	
	public void setRow(String[] row);

	public boolean next();

	public void close();

}
