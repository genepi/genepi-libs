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

import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CsvTableReader extends AbstractTableReader {

	private String[] header;

	private String[] currentLine;

	private Map<String, Integer> columns2Index = new HashMap<String, Integer>();

	private CSVReader reader;

	private String filename;

	public CsvTableReader(String filename, char seperator) {

		this.filename = filename;

		try {
			reader = new CSVReader(new FileReader(filename), seperator);
			header = reader.readNext();
			while (header != null && header[0].startsWith("#")) {
				header = reader.readNext();
			}
			if (header != null) {
				for (int i = 0; i < header.length; i++) {
					columns2Index.put(header[i], i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CsvTableReader(String filename, char seperator, boolean ignoreComment) {

		this.filename = filename;

		try {
			reader = new CSVReader(new FileReader(filename), seperator);
			header = reader.readNext();
			//while (header != null) {
			//	header = reader.readNext();
			//}
			if (header != null) {
				for (int i = 0; i < header.length; i++) {
					columns2Index.put(header[i], i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CsvTableReader(DataInputStream in, char seperator) {

		try {
			reader = new CSVReader(new InputStreamReader(in), seperator);
			header = reader.readNext();
			while (header != null && header[0].startsWith("#")) {
				header = reader.readNext();
			}

			if (header != null) {
				for (int i = 0; i < header.length; i++) {
					columns2Index.put(header[i], i);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public CsvTableReader(String filename, char seperator, int offset) {

		this.filename = filename;

		try {
			reader = new CSVReader(new FileReader(filename), seperator);
			header = reader.readNext();
			while (header[0].startsWith("#")) {
				header = reader.readNext();
			}

			String[] newHeader = new String[header.length + offset];
			for (int i = 0; i < offset + header.length; i++) {
				if (i < offset) {
					newHeader[i] = "test_" + i;
				} else {
					newHeader[i] = header[i - offset];
				}
			}
			header = newHeader;

			for (int i = 0; i < header.length; i++) {
				columns2Index.put(header[i], i);
			}
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
	public boolean next() {
		try {
			currentLine = reader.readNext();
			while (currentLine != null && currentLine[0].startsWith("#")) {
				currentLine = reader.readNext();
			}
			return currentLine != null;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return filename;
	}
}
