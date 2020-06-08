package genepi.io.csv;

import java.util.HashMap;
import java.util.Map;

import genepi.io.table.reader.CsvTableReader;

public class CsvFileCompare {

	public void compare(String filenameA, String filenameB, String column){		
		
		CsvTableReader reader = new CsvTableReader(filenameA, '\t');
		Map<String, String[]> rowsA = new HashMap<String, String[]>();
		
		while (reader.next()) {
			String value = reader.getString(column);
			rowsA.put(value, reader.getRow());
		}
		reader.close();
		
		CsvTableReader reader2 = new CsvTableReader(filenameB, '\t');

		while (reader2.next()) {
			String value = reader2.getString(column);
			String[] rowB = reader2.getRow();
			String[] rowA = rowsA.get(value);
			if (rowA == null){
				System.out.println(value + " is missing in " + filenameB);
			}else{
				for (int i = 0; i < rowA.length; i++){
					if (!rowA[i].equals(rowB[i])){
						System.out.println("Different values in " + value + ": '" + rowA[i] + "' and '"  + rowB[i]+"'");
					}
				}
			}
			
		}
		reader2.close();
		
	}
	
	public static void main(String[] args) {
		

		String filenameA = "C:\\Documents and Settings\\q010lf\\My Documents\\Daten\\Projekte\\WebCNV\\source\\GenepiWebCNV\\input\\penncnv-test\\CEU_NA06985_500k_chr1";


		String filenameB = "C:\\Documents and Settings\\q010lf\\My Documents\\Daten\\Projekte\\WebCNV\\source\\GenepiWebCNV\\input\\penncnv-test\\file-server.txt";
		
		new CsvFileCompare().compare(filenameA, filenameB, "Name");
	}
	
}
