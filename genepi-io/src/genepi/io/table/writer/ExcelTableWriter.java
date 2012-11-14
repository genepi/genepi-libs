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

package genepi.io.table.writer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelTableWriter extends AbstractTableWriter {

	private WritableWorkbook workbook;
	private WritableSheet sheet;
	private Object[] currentLine;
	private int row = 1;
	private Map<String, Integer> columns2Index = new HashMap<String, Integer>();

	public ExcelTableWriter(String filename) {
		try {
			workbook = Workbook.createWorkbook(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.createSheet("Table", 0);
	}

	@Override
	public void close() {
		try {
			for (int i = 0; i < currentLine.length; i++) {
				CellView c = new CellView();
				c.setAutosize(true);
				sheet.setColumnView(i, c);
			}
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getColumnIndex(String column) {
		return columns2Index.get(column);
	}

	@Override
	public boolean next() {
		for (int i = 0; i < currentLine.length; i++) {
			if (currentLine[i] != null) {
				try {
					if (currentLine[i] instanceof String) {
						sheet.addCell(new Label(i, row, currentLine[i]
								.toString()));
					}
					if (currentLine[i] instanceof Double) {
						sheet.addCell(new jxl.write.Number(i, row,
								(Double) currentLine[i]));
					}
					if (currentLine[i] instanceof Integer) {
						sheet.addCell(new jxl.write.Number(i, row,
								(Integer) currentLine[i]));
					}
				} catch (RowsExceededException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
			currentLine[i] = null;
		}
		row++;
		return true;
	}

	@Override
	public void setColumns(String[] columns) {
		WritableFont font = new WritableFont(WritableFont.ARIAL, 10,
				WritableFont.BOLD, false);
		WritableCellFormat fontFormat = new WritableCellFormat(font);
		currentLine = new Object[columns.length];
		for (int i = 0; i < columns.length; i++) {
			columns2Index.put(columns[i], i);
			currentLine[i] = null;
			try {
				sheet.addCell(new Label(i, 0, columns[i], fontFormat));
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
		}
		row = 1;
	}

	@Override
	public void setDouble(int column, double value) {
		currentLine[column] = value;
	}

	@Override
	public void setInteger(int column, int value) {
		currentLine[column] = value;
	}

	@Override
	public void setString(int column, String value) {
		currentLine[column] = value;
	}

	@Override
	public void setRow(String[] row) {
		currentLine = row;
	}

}
