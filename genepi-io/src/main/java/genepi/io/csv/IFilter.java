package genepi.io.csv;

import genepi.io.table.reader.ITableReader;

public interface IFilter {
	public boolean accept(ITableReader reader);
}
