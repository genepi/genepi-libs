package genepi.hadoop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsUtil {

	public static void get(String hdfs, String filename, Configuration conf)
			throws FileNotFoundException {

		FileOutputStream fos = new FileOutputStream(filename);
		try {

			FileSystem fileSystem = FileSystem.get(conf);
			FSDataInputStream is = fileSystem.open(new Path(hdfs));
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
			System.out.println("Export file " + hdfs + " done... (" + size
					+ " bytes)");

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public static boolean deleteDirectory(FileSystem fileSystem,
			String directory) {
		Path path = new Path(directory);
		try {
			if (fileSystem.exists(path)) {
				fileSystem.delete(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean deleteDirectory(String directory) {
		Configuration conf = new Configuration();
		FileSystem fileSystem;
		try {
			fileSystem = FileSystem.get(conf);
			return deleteDirectory(fileSystem, directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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

}
