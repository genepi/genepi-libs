package genepi.io.csv;

import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;

public class CsvFilter {

	public static final int INTEGER = 0;
	public static final int STRING = 2;

	public static void filter(String filename, String output, IFilter filter) {
		CsvTableReader reader = new CsvTableReader(filename, '\t');

		CsvTableWriter writer = new CsvTableWriter(output, '\t', false);
		writer.setColumns(reader.getColumns());

		while (reader.next()) {
			String[] row = reader.getRow();
			if (filter.accept(reader)) {
				writer.setRow(row);
				writer.next();
			}
		}
		reader.close();
		writer.close();

		System.out.println("Done!\n\n");
	}

}
