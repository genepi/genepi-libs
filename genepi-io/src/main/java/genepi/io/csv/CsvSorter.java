package genepi.io.csv;

import genepi.io.table.reader.CsvTableReader;
import genepi.io.table.writer.CsvTableWriter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class CsvSorter {

	public static final int INTEGER = 0;
	public static final int STRING = 2;

	public void sort(String filename, String output, final String[] column,
			final int[] type) {
		CsvTableReader reader = new CsvTableReader(filename, '\t');
		List<String[]> rows = new Vector<String[]>();
		System.out.println("Reading input file...");
		while (reader.next()) {
			rows.add(reader.getRow());
		}
		reader.close();

		final int index[] = new int[column.length];
		for (int i = 0; i < column.length; i++) {
			index[i] = reader.getColumnIndex(column[i]);
		}

		System.out.println("Sort...");
		Collections.sort(rows, new Comparator<String[]>() {

			@Override
			public int compare(String[] arg0, String[] arg1) {

				for (int i = 0; i < column.length; i++) {
					int result = 0;
					if (type[i] == INTEGER) {
						result = new Integer(arg0[index[i]])
								.compareTo(new Integer(arg1[index[i]]));
					} else {
						result = arg0[index[i]].compareTo(arg1[index[i]]);
					}

					if (result != 0) {
						return result;
					}
				}
				return 0;
			}
		});

		System.out.println("Writing output file...");
		CsvTableWriter writer = new CsvTableWriter(output, '\t', false);
		writer.setColumns(reader.getColumns());
		for (String[] row : rows) {
			writer.setRow(row);
			writer.next();
		}

		writer.close();

		System.out.println("Done!\n\n");
	}

	public static void main(String[] args) {
		String input = "C:\\Documents and Settings\\q010lf\\My Documents\\Daten\\Projekte\\WebCNV\\source\\GenepiWebCNV\\input\\penncnv-test\\CEU_NA06985_500k.txt";

		String columns[] = { "Chr", "Position" };

		int types[] = { STRING, INTEGER };

		String output = "C:\\Documents and Settings\\q010lf\\My Documents\\Daten\\Projekte\\WebCNV\\source\\GenepiWebCNV\\input\\penncnv-test\\CEU_NA06985_500k_sorted.txt";

		new CsvSorter().sort(input, output, columns, types);
	}
}
