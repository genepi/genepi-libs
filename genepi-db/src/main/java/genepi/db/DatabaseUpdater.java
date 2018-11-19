/*******************************************************************************
 * Copyright (C) 2009-2016 Lukas Forer and Sebastian Schönherr
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

package genepi.db;

import genepi.io.FileUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatabaseUpdater {

	protected static final Log log = LogFactory.getLog(DatabaseUpdater.class);

	private DatabaseConnector connector;

	private String oldVersion;

	private String currentVersion;

	private String filename;

	private InputStream updateFileStram;

	private boolean needUpdate = false;

	public DatabaseUpdater(DatabaseConnector connector, String filename, InputStream updateFileStram,
			String currentVersion) {

		this.connector = connector;
		this.filename = filename;
		this.updateFileStram = updateFileStram;
		this.currentVersion = currentVersion;

		oldVersion = readVersion(filename);

		needUpdate = (compareVersion(currentVersion, oldVersion) > 0);

	}

	public boolean update() {

		if (needUpdate) {

			try {

				log.info("Updating database from " + oldVersion + " to " + currentVersion + "....");

				executeSQLClasspath(updateFileStram, oldVersion, currentVersion);

				log.info("Updating database successful.");

				FileUtil.writeStringBufferToFile(filename, new StringBuffer(currentVersion));

			} catch (SQLException e) {

				log.error("Updating database failed.", e);
				return false;

			} catch (IOException e) {

				log.error("Updating database failed.", e);
				return false;

			} catch (URISyntaxException e) {

				log.error("Updating database failed.", e);
				return false;

			}

		}

		return true;

	}

	public boolean needUpdate() {
		return needUpdate;
	}

	public String readVersion(String versionFile) {

		File file = new File(versionFile);

		if (file.exists()) {

			try {

				return readFileAsString(versionFile);

			} catch (Exception e) {

				return "1.0.0";

			}

		} else {

			return "1.0.0";

		}

	}

	public static String readFileAsString(String filename) throws java.io.IOException, URISyntaxException {

		InputStream is = new FileInputStream(filename);

		DataInputStream in = new DataInputStream(is);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringBuilder builder = new StringBuilder();
		while ((strLine = br.readLine()) != null) {
			// builder.append("\n");
			builder.append(strLine);
		}

		in.close();

		return builder.toString();
	}

	public void executeSQLFile(String filename, String minVersion, String maxVersion)
			throws SQLException, IOException, URISyntaxException {

		String sqlContent = readFileAsStringFile(filename, minVersion, maxVersion);

		Connection connection = connector.getDataSource().getConnection();

		PreparedStatement ps = connection.prepareStatement(sqlContent);
		ps.executeUpdate();
		connection.close();
	}

	public static String readFileAsStringFile(String filename, String minVersion, String maxVersion)
			throws java.io.IOException, URISyntaxException {

		InputStream is = new FileInputStream(filename);

		DataInputStream in = new DataInputStream(is);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringBuilder builder = new StringBuilder();
		boolean reading = false;
		while ((strLine = br.readLine()) != null) {

			if (strLine.startsWith("--")) {

				String version = strLine.replace("--", "").trim();
				reading = (compareVersion(version, minVersion) > 0 && compareVersion(version, maxVersion) <= 0);
				if (reading) {
					log.info("Loading update for version " + version);
				}

			}

			if (reading) {
				builder.append("\n");
				builder.append(strLine);
			}
		}

		in.close();

		return builder.toString();
	}

	public void executeSQLClasspath(InputStream is, String minVersion, String maxVersion)
			throws SQLException, IOException, URISyntaxException {

		String sqlContent = readFileAsStringClasspath(is, minVersion, maxVersion);

		if (!sqlContent.isEmpty()) {
			Connection connection = connector.getDataSource().getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlContent);
			ps.executeUpdate();
			connection.close();
		}
	}

	public static String readFileAsStringClasspath(InputStream is, String minVersion, String maxVersion)
			throws java.io.IOException, URISyntaxException {

		DataInputStream in = new DataInputStream(is);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringBuilder builder = new StringBuilder();
		boolean reading = false;
		while ((strLine = br.readLine()) != null) {

			if (strLine.startsWith("--")) {

				String version = strLine.replace("--", "");
				reading = (compareVersion(version, minVersion) > 0 && compareVersion(version, maxVersion) <= 0);

				if (reading) {
					log.info("Loading update for version " + version);
				}

			}

			if (reading) {
				builder.append("\n");
				builder.append(strLine);
			}
		}

		in.close();

		return builder.toString();
	}

	public static int compareVersion(String version1, String version2) {

		String parts1[] = version1.split("-", 2);
		String parts2[] = version2.split("-", 2);

		String tiles1[] = parts1[0].split("\\.");
		String tiles2[] = parts2[0].split("\\.");

		for (int i = 0; i < tiles1.length; i++) {
			int number1 = Integer.parseInt(tiles1[i].trim());
			int number2 = Integer.parseInt(tiles2[i].trim());

			if (number1 != number2) {

				return number1 > number2 ? 1 : -1;

			}

		}

		if (parts1.length > 1) {
			if (parts2.length > 1) {
				return parts1[1].compareTo(parts2[1]);
			} else {
				return -1;
			}
		}else {
			if (parts2.length > 1) {
				return 1;
			}
		}

		return 0;

	}

}
