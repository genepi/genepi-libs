package genepi.io.text;

import static org.junit.Assert.assertEquals;
import java.io.IOException;

import org.junit.Test;

public class LineReaderTest {

	@Test
	public void testLoad() throws IOException {

		LineReader reader = new LineReader("test-data/testLoad.csv");
		int count = 0;

		while (reader.next()) {
			count++;
		}

		assertEquals(6, count);

		reader.close();

	}

	@Test
	public void testLoadWithEmptyLine() throws IOException {

		LineReader reader = new LineReader("test-data/testLoadWithEmptyLine.csv");
		int count = 0;

		while (reader.next()) {
			count++;
		}

		assertEquals(7, count);

		reader.close();

	}

	@Test
	public void testLoadWithEmptyLineInBetween() throws IOException {

		LineReader reader = new LineReader("test-data/testLoadWithEmptyLineBetween.csv");
		int count = 0;
		int countDuplicates = 0;
		String tmp = null;
		while (reader.next()) {
			if (tmp != null && tmp.equals(reader.get())) {
				countDuplicates++;
			}
			count++;
			tmp = reader.get();
		}

		assertEquals(7, count);
		assertEquals(0, countDuplicates);

		reader.close();

	}
}
