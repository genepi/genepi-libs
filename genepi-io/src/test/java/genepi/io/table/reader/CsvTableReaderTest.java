package genepi.io.table.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import genepi.io.table.exceptions.ColumnNotFoundException;

public class CsvTableReaderTest {

	@Test
	public void testLoad() {

		ITableReader reader = new CsvTableReader("test-data/testLoad.csv", ',');

		assertEquals("test-data/testLoad.csv", reader.toString());

		assertEquals(5, reader.getColumns().length);
		assertEquals(0, reader.getColumnIndex("COL1"));
		assertEquals(0, reader.getColumnIndex("Col1"));
		assertEquals(0, reader.getColumnIndex("col1"));
		assertEquals(1, reader.getColumnIndex("col2"));
		assertEquals(2, reader.getColumnIndex("col3"));
		assertEquals(3, reader.getColumnIndex("col4"));
		assertEquals(4, reader.getColumnIndex("col5"));

		assertTrue(reader.hasColumn("COL1"));
		assertFalse(reader.hasColumn("unknown"));

		int rows = 0;
		while (reader.next()) {
			rows++;
			assertEquals(rows, reader.getInteger("COL1"));
			assertEquals(rows, reader.getInteger("Col1"));
			assertEquals(rows, reader.getInteger("col1"));
			assertEquals("row_" + rows, reader.getString("COL2"));
			assertEquals("row_" + rows, reader.getString("Col2"));
			assertEquals("row_" + rows, reader.getString("col2"));

		}
		assertEquals(5, rows);

		reader.close();

	}
	
	@Test
	public void testLoadAndRowIterator() {

		ITableReader reader = new CsvTableReader("test-data/testLoad.csv", ',');

		assertEquals("test-data/testLoad.csv", reader.toString());

		assertEquals(5, reader.getColumns().length);
		assertEquals(0, reader.getColumnIndex("COL1"));
		assertEquals(0, reader.getColumnIndex("Col1"));
		assertEquals(0, reader.getColumnIndex("col1"));
		assertEquals(1, reader.getColumnIndex("col2"));
		assertEquals(2, reader.getColumnIndex("col3"));
		assertEquals(3, reader.getColumnIndex("col4"));
		assertEquals(4, reader.getColumnIndex("col5"));

		assertTrue(reader.hasColumn("COL1"));
		assertFalse(reader.hasColumn("unknown"));

		int rows = 0;
		for (Row row: reader) {
			rows++;
			assertEquals(rows, row.getInteger("COL1"));
			assertEquals(rows, row.getInteger("Col1"));
			assertEquals(rows, row.getInteger("col1"));
			assertEquals("row_" + rows, row.getString("COL2"));
			assertEquals("row_" + rows, row.getString("Col2"));
			assertEquals("row_" + rows, row.getString("col2"));

		}
		assertEquals(5, rows);

		reader.close();

	}
	

	@Test
	public void testLoadWithGzStream() throws FileNotFoundException, IOException {

		ITableReader reader = new CsvTableReader("test-data/testLoad.csv.gz", ',');

		assertEquals(5, reader.getColumns().length);
		assertEquals(0, reader.getColumnIndex("COL1"));
		assertEquals(0, reader.getColumnIndex("Col1"));
		assertEquals(0, reader.getColumnIndex("col1"));
		assertEquals(1, reader.getColumnIndex("col2"));
		assertEquals(2, reader.getColumnIndex("col3"));
		assertEquals(3, reader.getColumnIndex("col4"));
		assertEquals(4, reader.getColumnIndex("col5"));

		assertTrue(reader.hasColumn("COL1"));
		assertFalse(reader.hasColumn("unknown"));

		int rows = 0;
		while (reader.next()) {
			rows++;
			assertEquals(rows, reader.getInteger("COL1"));
			assertEquals(rows, reader.getInteger("Col1"));
			assertEquals(rows, reader.getInteger("col1"));
			assertEquals("row_" + rows, reader.getString("COL2"));
			assertEquals("row_" + rows, reader.getString("Col2"));
			assertEquals("row_" + rows, reader.getString("col2"));

		}
		assertEquals(5, rows);

		reader.close();

	}

	@Test
	public void testLoadWithHeader() {

		ITableReader reader = new CsvTableReader("test-data/testLoadWithHeader.csv", ',');

		assertEquals(5, reader.getColumns().length);
		assertEquals(0, reader.getColumnIndex("COL1"));
		assertEquals(0, reader.getColumnIndex("Col1"));
		assertEquals(0, reader.getColumnIndex("col1"));
		assertEquals(1, reader.getColumnIndex("col2"));
		assertEquals(2, reader.getColumnIndex("col3"));
		assertEquals(3, reader.getColumnIndex("col4"));
		assertEquals(4, reader.getColumnIndex("col5"));

		assertTrue(reader.hasColumn("COL1"));
		assertFalse(reader.hasColumn("unknown"));

		int rows = 0;
		while (reader.next()) {
			rows++;
			assertEquals(rows, reader.getInteger("COL1"));
			assertEquals(rows, reader.getInteger("Col1"));
			assertEquals(rows, reader.getInteger("col1"));
			assertEquals("row_" + rows, reader.getString("COL2"));
			assertEquals("row_" + rows, reader.getString("Col2"));
			assertEquals("row_" + rows, reader.getString("col2"));

		}
		assertEquals(5, rows);

		reader.close();

	}

	@Test
	public void testLoadWithHeaderIgnoreComment() {

		ITableReader reader = new CsvTableReader("test-data/testLoadWithHeader.csv", ',', true);

		assertEquals(5, reader.getColumns().length);
		assertEquals(0, reader.getColumnIndex("#hCOL1"));
		assertEquals(0, reader.getColumnIndex("#hCol1"));
		assertEquals(0, reader.getColumnIndex("#hcol1"));
		assertEquals(1, reader.getColumnIndex("hcol2"));
		assertEquals(2, reader.getColumnIndex("hcol3"));
		assertEquals(3, reader.getColumnIndex("hcol4"));
		assertEquals(4, reader.getColumnIndex("hcol5"));

		assertTrue(reader.hasColumn("#hCOL1"));
		assertFalse(reader.hasColumn("unknown"));

		int rows = 0;
		while (reader.next()) {
			rows++;
			if (rows == 1) {
				assertEquals("Col1", reader.getString("#hCOL1"));
				assertEquals("Col1", reader.getString("#hCol1"));
				assertEquals("Col1", reader.getString("#hcol1"));
				assertEquals("Col2", reader.getString("hCOL2"));
				assertEquals("Col2", reader.getString("hCol2"));
				assertEquals("Col2", reader.getString("hcol2"));
			} else {
				assertEquals(rows - 1, reader.getInteger("#hCOL1"));
				assertEquals(rows - 1, reader.getInteger("#hCol1"));
				assertEquals(rows - 1, reader.getInteger("#hcol1"));
				assertEquals("row_" + (rows - 1), reader.getString("hCOL2"));
				assertEquals("row_" + (rows - 1), reader.getString("hCol2"));
				assertEquals("row_" + (rows - 1), reader.getString("hcol2"));
			}
		}

		assertEquals(6, rows);
		reader.close();
	}

	@Test
	public void testLoadWithComment() {

		ITableReader reader = new CsvTableReader("test-data/testLoadWithComment.csv", ',');

		assertEquals(5, reader.getColumns().length);
		assertEquals(0, reader.getColumnIndex("COL1"));
		assertEquals(0, reader.getColumnIndex("Col1"));
		assertEquals(0, reader.getColumnIndex("col1"));
		assertEquals(1, reader.getColumnIndex("col2"));
		assertEquals(2, reader.getColumnIndex("col3"));
		assertEquals(3, reader.getColumnIndex("col4"));
		assertEquals(4, reader.getColumnIndex("col5"));

		assertTrue(reader.hasColumn("COL1"));
		assertFalse(reader.hasColumn("unknown"));

		int rows = 0;
		while (reader.next()) {
			rows++;
			assertEquals(rows, reader.getInteger("COL1"));
			assertEquals(rows, reader.getInteger("Col1"));
			assertEquals(rows, reader.getInteger("col1"));
			assertEquals("row_" + rows, reader.getString("COL2"));
			assertEquals("row_" + rows, reader.getString("Col2"));
			assertEquals("row_" + rows, reader.getString("col2"));

		}
		assertEquals(5, rows);

		reader.close();

	}
	
	@Test(expected = ColumnNotFoundException.class)
	public void testColumnNotFound() {

		ITableReader reader = new CsvTableReader("test-data/testLoad.csv", ',');

		assertFalse(reader.hasColumn("unknown"));
		assertEquals("test-data/testLoad.csv", reader.toString());
		
		while (reader.next()) {
			assertEquals(null, reader.getInteger("unknown"));
			assertEquals(true, false);
		}

		reader.close();

	}

}
