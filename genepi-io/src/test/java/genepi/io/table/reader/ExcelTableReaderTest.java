package genepi.io.table.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import genepi.io.table.exceptions.ColumnNotFoundException;

public class ExcelTableReaderTest {

	@Test
	public void testLoad() {

		ITableReader reader = new ExcelTableReader("test-data/testLoad.xls");

		assertEquals("test-data/testLoad.xls", reader.toString());
		
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
	public void testGetDouble() {

		ITableReader reader = new ExcelTableReader("test-data/testLoad.xls");

		assertEquals("test-data/testLoad.xls", reader.toString());
		
		assertEquals(5, reader.getColumns().length);

		int rows = 0;
		while (reader.next()) {
			rows++;
			double result = 1.1 * rows;
			assertEquals(result, reader.getDouble("COL5"),0.001);
		}
		assertEquals(5, rows);

		reader.close();

	}
	
	@Test(expected = ColumnNotFoundException.class)
	public void testColumnNotFoundInteger() {

		ITableReader reader = new ExcelTableReader("test-data/testLoad.xls");
		assertFalse(reader.hasColumn("unknown"));
		assertEquals("test-data/testLoad.xls", reader.toString());
		
		while (reader.next()) {
			assertEquals(null, reader.getInteger("unknown"));
			assertEquals(true, false);
		}

		reader.close();

	}
	
	@Test(expected = ColumnNotFoundException.class)
	public void testColumnNotFoundDouble() {

		ITableReader reader = new ExcelTableReader("test-data/testLoad.xls");
		assertFalse(reader.hasColumn("unknown"));
		assertEquals("test-data/testLoad.xls", reader.toString());
		
		while (reader.next()) {
			assertEquals(null, reader.getDouble("unknown"));
			assertEquals(true, false);
		}

		reader.close();

	}
	
	@Test(expected = ColumnNotFoundException.class)
	public void testColumnNotFoundString() {

		ITableReader reader = new ExcelTableReader("test-data/testLoad.xls");
		assertFalse(reader.hasColumn("unknown"));
		assertEquals("test-data/testLoad.xls", reader.toString());
		
		while (reader.next()) {
			assertEquals(null, reader.getString("unknown"));
			assertEquals(true, false);
		}

		reader.close();

	}

}
