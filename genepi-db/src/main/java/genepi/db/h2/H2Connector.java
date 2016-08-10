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

package genepi.db.h2;

import genepi.db.DatabaseConnector;
import genepi.io.FileUtil;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class H2Connector implements DatabaseConnector {

	protected static final Log log = LogFactory.getLog(H2Connector.class);

	private BasicDataSource dataSource;

	private String path;
	private String user;
	private String password;
	private boolean multiuser = false;
	private boolean exists = false;

	public H2Connector(String path, String user, String password,
			boolean multiuser) {
		this.path = path;
		this.user = user;
		this.password = password;
		this.multiuser = multiuser;
	}

	public boolean createBackup(String folder) {

		File file = new File(path + ".h2.db");
		File file2 = new File(path + ".mv.db");
		
		boolean exists = file.exists() || file2.exists();

		if (exists) {

			FileUtil.copyDirectory(file.getParent(), folder);

		}

		log.info("Created backup file " + folder);

		return true;

	}

	public void connect() throws SQLException {

		File file = new File(path + ".h2.db");
		File file2 = new File(path + ".mv.db");
		exists = file.exists() || file2.exists();

		log.debug("Establishing connection to " + user + "@" + path);

		if (DbUtils.loadDriver("org.h2.Driver")) {
			try {
				dataSource = new BasicDataSource();

				dataSource.setDriverClassName("org.h2.Driver");
				if (multiuser) {
					dataSource.setUrl("jdbc:h2:" + "./" + path
							+ ";AUTO_SERVER=TRUE");
				} else {
					dataSource.setUrl("jdbc:h2:" + "./" + path);
				}
				dataSource.setUsername(user);
				dataSource.setPassword(password);
				// dataSource.setMaxActive(1000);
				// dataSource.setMaxWait(10000);
				dataSource.setMaxIdle(10000);
				dataSource.setDefaultAutoCommit(true);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			log.error("H2 Driver Class not found");
		}

	}

	public void disconnect() throws SQLException {
		dataSource.close();

	}

	public boolean isNewDatabase() {
		return !exists;
	}

	public void executeSQL(InputStream is) throws SQLException, IOException,
			URISyntaxException {

		String sqlContent = readFileAsString(is);
		if (!sqlContent.isEmpty()) {
			Connection connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement(sqlContent);
			ps.executeUpdate();
			connection.close();
		}
	}

	public static String readFileAsString(InputStream is)
			throws java.io.IOException, URISyntaxException {

		DataInputStream in = new DataInputStream(is);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		StringBuilder builder = new StringBuilder();
		while ((strLine = br.readLine()) != null) {
			builder.append("\n");
			builder.append(strLine);
		}

		in.close();

		return builder.toString();
	}

	public BasicDataSource getDataSource() {
		return dataSource;
	}
}
