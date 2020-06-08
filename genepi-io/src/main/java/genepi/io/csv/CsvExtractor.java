package genepi.io.csv;

import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;
import genepi.io.table.writer.ITableWriter;

public class CsvExtractor {

	public void extract(String input, String columns[], String output) {

		CsvTableReader reader = new CsvTableReader(input, '\t');
		ITableWriter writer = new CsvTableWriter(output, '\t');
		writer.setColumns(columns);
		while (reader.next()) {
			for (String column : columns) {
				try {
					writer.setString(column, reader.getString(column));
				} catch (Exception e) {
					System.out.println("error reading " + column);
				}
			}
			writer.next();
		}
		writer.close();
		reader.close();
	}

}
