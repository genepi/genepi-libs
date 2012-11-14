package genepi.io.csv;

import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class CsvMapper {

	public static final int INTEGER = 0;
	public static final int STRING = 2;

	public void map(String filename, String output, String mapping,
			String column, String columnId, String columnMapping) {

		CsvTableReader readerMapping = new CsvTableReader(mapping, '\t');
		Map<String, String> mappingValues = new HashMap<String, String>();
		System.out.println("Reading Mapping file...");
		while (readerMapping.next()) {
			mappingValues.put(readerMapping.getString(columnId), readerMapping
					.getString(columnMapping));
		}
		readerMapping.close();

		CsvTableReader reader = new CsvTableReader(filename, '\t');
		CsvTableWriter writer = new CsvTableWriter(output, '\t', false);
		writer.setColumns(reader.getColumns());
		List<String[]> rows = new Vector<String[]>();
		System.out.println("Reading input file...");

		while (reader.next()) {
			rows.add(reader.getRow());
		}
		reader.close();

		int index = reader.getColumnIndex(column);
		
		System.out.println("Writing output file...");
		for (String[] row : rows) {
			row[index] = mappingValues.get(row[index]);
			writer.setRow(row);
			writer.next();
		}

		writer.close();

		System.out.println("Done!\n\n");
	}

}
