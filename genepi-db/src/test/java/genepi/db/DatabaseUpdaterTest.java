package genepi.db;

import junit.framework.TestCase;

public class DatabaseUpdaterTest extends TestCase {

	public void testCompareVersion() {

		assertEquals(0, DatabaseUpdater.compareVersion("1.0.0", "1.0.0"));
		assertEquals(0, DatabaseUpdater.compareVersion("1.1.0", "1.1.0"));
		assertEquals(0, DatabaseUpdater.compareVersion("1.1.1", "1.1.1"));
		assertEquals(0, DatabaseUpdater.compareVersion("1.1.1-alpha", "1.1.1-alpha"));
		assertEquals(0, DatabaseUpdater.compareVersion("1.1.1-RC1", "1.1.1-RC1"));
		assertEquals(0, DatabaseUpdater.compareVersion("1.1.1-beta", "1.1.1-beta"));

		assertEquals(1, DatabaseUpdater.compareVersion("2.0.0", "1.0.0"));
		assertEquals(1, DatabaseUpdater.compareVersion("1.2.0", "1.1.0"));
		assertEquals(1, DatabaseUpdater.compareVersion("1.1.2", "1.1.1"));
		assertEquals(1, DatabaseUpdater.compareVersion("2.0.0", "1.20.20"));
		assertEquals(1, DatabaseUpdater.compareVersion("2.0.0", "2.0.0-RC5"));
		assertEquals(1, DatabaseUpdater.compareVersion("2.0.0-RC2", "2.0.0-RC1"));
		assertEquals(1, DatabaseUpdater.compareVersion("2.0.1", "2.0.0-RC5"));

		assertEquals(-1, DatabaseUpdater.compareVersion("1.0.0", "2.0.0"));
		assertEquals(-1, DatabaseUpdater.compareVersion("1.1.0", "1.2.0"));
		assertEquals(-1, DatabaseUpdater.compareVersion("1.1.1", "1.1.2"));
		assertEquals(-1, DatabaseUpdater.compareVersion("1.20.20", "2.0.0"));
		assertEquals(-1, DatabaseUpdater.compareVersion("2.0.0-RC5", "2.0.0"));
		assertEquals(-1, DatabaseUpdater.compareVersion("2.0.0-RC1", "2.0.0-RC2"));
		assertEquals(-1, DatabaseUpdater.compareVersion("2.0.0-RC5", "2.0.1"));

	}

}
