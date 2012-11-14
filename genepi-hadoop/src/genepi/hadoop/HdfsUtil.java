package genepi.hadoop;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsUtil {

	public static void get(String hdfs, String filename,
			Configuration configuration) throws IOException {

		FileSystem fileSystem = FileSystem.get(configuration);
		Path path = new Path(hdfs);

		if (fileSystem.isDirectory(path)) {

			// merge
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					filename));
			FileStatus[] files = fileSystem.listStatus(new Path(hdfs));

			for (FileStatus file : files) {
				if (!file.isDir()) {

					FSDataInputStream is = fileSystem.open(file.getPath());
					byte[] readData = new byte[1024];
					int i = is.read(readData);
					long size = i;
					while (i != -1) {
						fos.write(readData, 0, i);
						i = is.read(readData);
						size += i;
					}
					is.close();

				}
			}
			fos.close();

		} else {

			FileOutputStream fos = new FileOutputStream(filename);

			FSDataInputStream is = fileSystem.open(path);
			byte[] readData = new byte[1024];
			int i = is.read(readData);
			long size = i;
			while (i != -1) {
				fos.write(readData, 0, i);
				i = is.read(readData);
				size += i;
			}
			is.close();

			fos.close();

		}

	}

	public static void get(String hdfs, String filename) throws IOException {
		Configuration configuration = new Configuration();
		get(hdfs, filename, configuration);
	}

	public static void put(String filename, String target, Configuration conf) {
		try {

			FileInputStream in = new FileInputStream(filename);

			FileSystem fileSystem = FileSystem.get(conf);
			FSDataOutputStream out = fileSystem.create(new Path(target));

			IOUtils.copyBytes(in, out, fileSystem.getConf());

			System.out.println("Import file " + filename + " done...("
					+ out.size() + " bytes)");

			IOUtils.closeStream(in);
			IOUtils.closeStream(out);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void put(String filename, String target) {
		Configuration configuration = new Configuration();
		put(filename, target, configuration);
	}

	public static boolean delete(String directory, Configuration configuration) {
		Path path = new Path(directory);
		try {
			FileSystem fileSystem = FileSystem.get(configuration);
			if (fileSystem.exists(path)) {
				fileSystem.delete(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean delete(String directory) {
		Configuration configuration = new Configuration();
		return delete(directory, configuration);
	}

	public static String path(String... paths) {
		String result = "";
		for (int i = 0; i < paths.length; i++) {
			String path = paths[i];
			if (!path.isEmpty()) {
				if (i > 0 && !path.startsWith("/") && !result.endsWith("/")) {
					result += "/" + path;
				} else {
					result += path;
				}
			}
		}
		return result;
	}

	public static void getAsZip(String zipFile, String hdfs, boolean merge,
			Configuration configuration) {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFile));

			// Compress the files

			FileSystem fileSystem = FileSystem.get(configuration);
			Path pathFolder = new Path(hdfs);
			FileStatus[] files = fileSystem.listStatus(pathFolder);

			// Add ZIP entry to output stream.
			if (merge) {
				out.putNextEntry(new ZipEntry(pathFolder.getName()));
			}

			for (FileStatus file : files) {
				Path path = file.getPath();
				if (!file.isDir() && !file.getPath().getName().startsWith("_")) {
					FSDataInputStream in = fileSystem.open(path);
					if (!merge) {
						out.putNextEntry(new ZipEntry(path.getName()));
					}
					// Transfer bytes from the file to the ZIP file
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}

					// Complete the entry
					if (!merge) {
						out.closeEntry();
					}

					in.close();
				}
			}
			if (merge) {
				out.closeEntry();
			}

			// Complete the ZIP file
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void getAsZip(String zipFile, String hdfs, boolean merge) {
		Configuration configuration = new Configuration();
		getAsZip(zipFile, hdfs, merge, configuration);
	}

	public static void putZip(String filename, String folder,
			Configuration configuration) {
		try {

			FileSystem filesystem = FileSystem.get(configuration);

			ZipInputStream zipinputstream = new ZipInputStream(
					new FileInputStream(filename));

			byte[] buf = new byte[1024];
			ZipEntry zipentry = zipinputstream.getNextEntry();

			while (zipentry != null) {
				// for each entry to be extracted
				String entryName = zipentry.getName();

				if (!zipentry.isDirectory()) {
					String target = HdfsUtil.path(folder, entryName);

					FSDataOutputStream out = filesystem
							.create(new Path(target));

					int n;
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
						out.write(buf, 0, n);
					out.close();

					zipinputstream.closeEntry();
				}

				zipentry = zipinputstream.getNextEntry();

			}// while

			zipinputstream.close();
			System.out.println("done extracting");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void putZip(String filename, String folder) {
		Configuration configuration = new Configuration();
		putZip(filename, folder, configuration);
	}

}
