package genepi.io.csv;

import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;

import java.util.List;
import java.util.Vector;

public class CsvColumnDeleter {

	public static final int INTEGER = 0;
	public static final int STRING = 2;

	public void sort(String filename, String output, final String[] column) {
		CsvTableReader reader = new CsvTableReader(filename, '\t');
		CsvTableWriter writer = new CsvTableWriter(output, '\t', false);

		List<String[]> rows = new Vector<String[]>();
		System.out.println("Reading input file...");
		while (reader.next()) {
			rows.add(reader.getRow());
		}
		reader.close();

		List<Integer> index = new Vector<Integer>();
		for (int i = 0; i < column.length; i++) {
			index.add(reader.getColumnIndex(column[i]));
		}

		String[] header = new String[reader.getColumns().length - column.length];
		int count = 0;
		for (int i = 0; i < reader.getColumns().length; i++) {
			if (!index.contains(i)) {
				header[count] = reader.getColumns()[i];
				count++;
			}
		}
		writer.setColumns(header);

		System.out.println("Writing output file...");
		for (String[] row : rows) {
			count = 0;

			for (int i = 0; i < reader.getColumns().length; i++) {
				if (!index.contains(i)) {
					writer.setString(count, row[i]);
					count++;
				}
			}
			writer.next();
		}

		writer.close();

		System.out.println("Done!\n\n");
	}

}
