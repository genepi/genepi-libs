package genepi.io.table;

import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.reader.ExcelTableReader;
import genepi.io.table.reader.ITableReader;

public class TableReaderFactory {

	public static ITableReader getReader(String filename) {

		if (filename.toLowerCase().endsWith(".xls")) {
			return new ExcelTableReader(filename);
		} else {
			return new CsvTableReader(filename, '\t');
		}

	}
}
