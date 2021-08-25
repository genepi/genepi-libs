package genepi.io.table.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxTableReader extends AbstractTableReader {

	private FileInputStream file;

	private XSSFWorkbook workbook;

	private XSSFSheet sheet;

	private Iterator<org.apache.poi.ss.usermodel.Row> rowIterator;

	private Map<String, Integer> columns2Index = new HashMap<String, Integer>();

	private String[] currentLine;

	public XlsxTableReader(String filename) throws IOException {
		this(filename, null);
	}

	public XlsxTableReader(String filename, String sheetName) throws IOException {
		file = new FileInputStream(new File(filename));

		workbook = new XSSFWorkbook(file);

		if (sheetName != null) {
			sheet = workbook.getSheet(sheetName);
		} else {
			sheet = workbook.getSheetAt(0);
		}

		// Iterate through each rows one by one
		rowIterator = sheet.iterator();
		if (!rowIterator.hasNext()) {
			throw new IOException("No header found");
		}
		org.apache.poi.ss.usermodel.Row row = rowIterator.next();
		Iterator<Cell> cellIterator = row.cellIterator();

		int i = 0;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			String value = cell.getStringCellValue();
			columns2Index.put(value.toLowerCase(), i);
			i++;
		}

	}

	@Override
	public String[] getColumns() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean next() {
		if (!rowIterator.hasNext()) {
			return false;
		}

		org.apache.poi.ss.usermodel.Row row = rowIterator.next();

		currentLine = new String[columns2Index.size()];

		for (int i = 0; i < currentLine.length; i++) {
			String value = "";
			Cell cell = row.getCell(i);
			if (cell != null) {
				CellType type = cell.getCellType();
				if (type == CellType.NUMERIC) {
					value = "" + cell.getNumericCellValue();
				} else if (type == CellType.BOOLEAN) {
					value = "" + cell.getBooleanCellValue();
				} else {
					value = cell.getStringCellValue();
				}
			}
			currentLine[i] = value;
		}

		return true;
	}

	@Override
	public int getColumnIndex(String column) {
		return columns2Index.get(column.toLowerCase().trim());
	}

	@Override
	public boolean hasColumn(String column) {
		return columns2Index.containsKey(column.toLowerCase().trim());
	}

	@Override
	public void close() {
		try {
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] getRow() {
		return currentLine;
	}

}
