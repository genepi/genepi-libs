/*******************************************************************************
 * CONAN: Copy Number Variation Analysis Software for
 * Next Generation Genome-Wide Association Studies
 * 
 * Copyright (C) 2009, 2010 Lukas Forer
 *  
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *  
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package genepi.io.table.reader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelTableReader extends AbstractTableReader {

	private String header[];

	private Sheet sheet;

	private Workbook workbook;

	private Map<String, Integer> columns2Index = new HashMap<String, Integer>();

	private int currentRow;

	private int columnCount;

	private String[] currentLine;

	private String filename;

	public ExcelTableReader(String filename) {

		this.filename = filename;

		try {
			workbook = Workbook.getWorkbook(new File(filename));
			sheet = workbook.getSheet(0);

			columnCount = sheet.getColumns();
			header = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				header[i] = sheet.getCell(i, 0).getContents();
				columns2Index.put(header[i], i);
			}
			currentRow = 0;
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getColumnIndex(String column) {
		return columns2Index.get(column);
	}

	@Override
	public String[] getColumns() {
		return header;
	}

	@Override
	public String[] getRow() {
		return currentLine;
	}

	@Override
	public double getDouble(int column) {
		return ((NumberCell) sheet.getCell(column, currentRow)).getValue();
	}

	@Override
	public boolean next() {
		currentRow++;
		if (currentRow < sheet.getRows()) {
			while (sheet.getCell(0, currentRow).isHidden()
					&& currentRow < sheet.getRows()) {
				currentRow++;
				if (currentRow >= sheet.getRows()) {
					return false;
				}
			}
			currentLine = new String[columnCount];
			for (int i = 0; i < columnCount; i++) {
				currentLine[i] = sheet.getCell(i, currentRow).getContents();
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void close() {
		workbook.close();
	}

	@Override
	public String toString() {
		return filename;
	}

}
