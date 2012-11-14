package genepi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;

public class FileUtil {

	public static boolean deleteFile(String filename) {
		return new File(filename).delete();
	}

	public static String[] getFiles(String path, String ext) {
		File dir = new File(path);
		File[] files = dir.listFiles(new WildCardFileFilter(ext));

		String[] names = new String[files.length];

		for (int i = 0; i < names.length; i++) {
			names[i] = files[i].getAbsolutePath();
		}

		return names;
	}

	public static void createDirectory(String dir) {
		File output = new File(dir);
		if (!output.exists()) {
			output.mkdirs();
		}
	}

	public static boolean copyDirectory(String source, String target) {

		String[] files = getFiles(source, "*.*");
		for (String sourceFile : files) {
			String name = new File(sourceFile).getName();
			String targetFile = FileUtil.path(target, name);
			copy(sourceFile, targetFile);
		}

		return true;
	}

	static public boolean deleteDirectory(String path) {
		return deleteDirectory(new File(path));
	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public static boolean copy(String source, String target) {

		try {
			// create FileInputStream object for source file
			FileInputStream fin = new FileInputStream(source);

			// create FileOutputStream object for destination file
			FileOutputStream fout = new FileOutputStream(target);

			byte[] b = new byte[1024];
			int noOfBytes = 0;

			// read bytes from source file and write to destination file
			while ((noOfBytes = fin.read(b)) != -1) {
				fout.write(b, 0, noOfBytes);
			}

			// close the streams
			fin.close();
			fout.close();

		} catch (FileNotFoundException fnf) {
			System.out.println("Specified file not found :" + fnf);
		} catch (IOException ioe) {
			System.out.println("Error while copying file :" + ioe);
		}
		return true;

	}

	public static boolean copy2(String source, String target) {

		try {

			File inputFile = new File(source);
			File outputFile = new File(target);

			FileReader in = new FileReader(inputFile);
			FileWriter out = new FileWriter(outputFile);

			int c;

			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();

			return true;

		} catch (Exception e) {

			return false;

		}

	}

	public static String path(String... paths) {
		String result = "";
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (path != null && !path.isEmpty()) {
				if (i > 0 && !path.startsWith(File.separator)
						&& !result.endsWith(File.separator)) {
					if (result.isEmpty()) {
						result += path;
					} else {
						result += File.separator + path;
					}
				} else {
					result += path;
				}
			}
		}
		return result;
	}

	public static String getFilename(String filename) {
		return new File(filename).getName();
	}

	public static void writeStringBufferToFile(String filename,
			StringBuffer buffer) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
			String outText = buffer.toString();
			out.write(outText);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String readFileAsString(String filePath) {
		try {
			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {

			return "";
		}
	}

	public static String readFileAsString(InputStream in) {
		try {
			StringBuffer fileData = new StringBuffer(1000);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {

			return "";
		}
	}

	public static InputStream getInputStream(String filename)
			throws IOException {

		// HTTP
		if (filename.startsWith("http://")) {

			URL url = new URL(filename);
			return url.openStream();

			// FTP
		} else if (filename.startsWith("ftp://")) {
			filename = filename.replace("ftp://", "ftp://anonymous:Password@");

			URL url = new URL(filename);
			URLConnection conn = url.openConnection();

			return conn.getInputStream();
		} else {
			filename = filename.replace("file://", "");
			return new FileInputStream(filename);

		}

	}

	public static InputStream getInputStream(String filename,
			final String username, final String password) throws IOException {

		// HTTP
		if (filename.startsWith("http://")) {
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password
							.toCharArray());
				}
			});

			URL url = new URL(filename);
			return url.openStream();

			// FTP
		} else if (filename.startsWith("ftp://")) {
			filename = filename.replace("ftp://", "ftp://" + username + ":"
					+ password + "@");

			URL url = new URL(filename);
			URLConnection conn = url.openConnection();

			return conn.getInputStream();

		} else {
			filename = filename.replace("file://", "");
			return new FileInputStream(filename);
		}
	}

}