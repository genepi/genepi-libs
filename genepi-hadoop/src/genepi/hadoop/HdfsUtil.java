package genepi.hadoop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.util.LineReader;

public class HdfsUtil {

	static Configuration defaultConfiguration = new Configuration();

	public static void setDefaultConfiguration(Configuration defaultConfiguration) {
		HdfsUtil.defaultConfiguration = defaultConfiguration;
	}

	public static Configuration getConfiguration() {
		return new Configuration(defaultConfiguration);
	}

	public static FileSystem getFileSystem() throws IOException {
		Configuration configuration = getConfiguration();
		FileSystem fileSystem = FileSystem.get(configuration);
		return fileSystem;
	}

	public static void get(String hdfs, String filename, Configuration configuration) throws IOException {

		FileSystem fileSystem = FileSystem.get(configuration);
		Path path = new Path(hdfs);

		if (fileSystem.isDirectory(path)) {

			// merge
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(filename));
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

	public static void getFolder(String hdfs, String filename, Configuration configuration) throws IOException {

		FileSystem fileSystem = FileSystem.get(configuration);
		Path path = new Path(hdfs);

		FileStatus[] files = fileSystem.listStatus(path);

		new File(filename).mkdirs();

		for (FileStatus file : files) {
			if (!file.isDir()) {
				DataOutputStream fos = new DataOutputStream(
						new FileOutputStream(filename + "/" + file.getPath().getName()));
				FSDataInputStream is = fileSystem.open(file.getPath());
				byte[] readData = new byte[1024];
				int i = is.read(readData);
				while (i != -1) {
					fos.write(readData, 0, i);
					i = is.read(readData);
				}
				is.close();
				fos.close();

			} else {
				getFolder(HdfsUtil.path(hdfs, file.getPath().getName()), filename + "/" + file.getPath().getName(),
						configuration);
			}
		}

	}

	public static void getFolder(String hdfs, String filename) throws IOException {
		Configuration configuration = getConfiguration();
		getFolder(hdfs, filename, configuration);
	}

	public static void get(String hdfs, String filename) throws IOException {
		Configuration configuration = getConfiguration();
		get(hdfs, filename, configuration);
	}

	public static boolean exists(String filename, Configuration conf) {
		try {

			Path path = new Path(filename);
			FileSystem fileSystem = FileSystem.get(conf);
			return fileSystem.exists(path);

		} catch (Exception e) {
			return false;
		}

	}

	public static boolean copy(String source, String target) {
		Configuration configuration = getConfiguration();
		return copy(source, target, configuration);
	}

	public static boolean copy(String source, String target, Configuration conf) {

		try {

			Path pathSource = new Path(source);
			Path pathTarget = new Path(target);
			FileSystem fileSystem = FileSystem.get(conf);

			return FileUtil.copy(fileSystem, pathSource, fileSystem, pathTarget, false, conf);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static boolean exists(String filename) {
		Configuration configuration = getConfiguration();
		return exists(filename, configuration);

	}

	public static void put(String filename, String target, Configuration conf) {
		try {

			File file = new File(filename);

			if (file.isDirectory()) {

				File[] files = file.listFiles();
				for (File subFile : files) {
					put(subFile.getPath(), HdfsUtil.path(target, subFile.getName()));
				}

			} else {

				FileInputStream in = new FileInputStream(filename);

				FileSystem fileSystem = FileSystem.get(conf);
				FSDataOutputStream out = fileSystem.create(new Path(target));

				IOUtils.copyBytes(in, out, fileSystem.getConf());

				IOUtils.closeStream(in);
				IOUtils.closeStream(out);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void put(String filename, String target) {
		Configuration configuration = getConfiguration();
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
		Configuration configuration = getConfiguration();
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

	public static void getAsZip(String zipFile, String hdfs, boolean merge, Configuration configuration) {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

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
		Configuration configuration = getConfiguration();
		getAsZip(zipFile, hdfs, merge, configuration);
	}

	public static void putZip(String filename, String folder, Configuration configuration) {
		try {

			FileSystem filesystem = FileSystem.get(configuration);

			ZipInputStream zipinputstream = new ZipInputStream(new FileInputStream(filename));

			byte[] buf = new byte[1024];
			ZipEntry zipentry = zipinputstream.getNextEntry();

			while (zipentry != null) {
				// for each entry to be extracted
				String entryName = zipentry.getName();

				if (!zipentry.isDirectory()) {
					String target = HdfsUtil.path(folder, entryName);

					FSDataOutputStream out = filesystem.create(new Path(target));

					int n;
					while ((n = zipinputstream.read(buf, 0, 1024)) > -1)
						out.write(buf, 0, n);
					out.close();

					zipinputstream.closeEntry();
				}

				zipentry = zipinputstream.getNextEntry();

			} // while

			zipinputstream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void putTarGz(String filename, String folder) {
		Configuration configuration = getConfiguration();
		putTarGz(filename, folder, configuration);
	}

	public static void putTarGz(String filename, String folder, Configuration configuration) {

		int BUFFER_SIZE = 1024;

		try {

			FileSystem filesystem = FileSystem.get(configuration);

			GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(new FileInputStream(filename));
			TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn);

			TarArchiveEntry entry;

			while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
				// for each entry to be extracted
				String entryName = entry.getName();

				if (!entry.isDirectory()) {
					String target = HdfsUtil.path(folder, entryName);

					FSDataOutputStream out = filesystem.create(new Path(target));

					int count;
					byte data[] = new byte[BUFFER_SIZE];
					while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
						out.write(data, 0, count);
					}
					out.close();

				}

			}

			tarIn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void putZip(String filename, String folder) {
		Configuration configuration = getConfiguration();
		putZip(filename, folder, configuration);
	}

	public static void mergeAndGz(String local, String hdfs, boolean removeHeader, String ext)
			throws FileNotFoundException, IOException {
		GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(local));
		merge(out, hdfs, removeHeader, ext);
	}

	public static void merge(String local, String hdfs, boolean removeHeader) throws IOException {
		FileOutputStream out = new FileOutputStream(local);
		merge(out, hdfs, removeHeader, null);
	}

	public static void merge(OutputStream out, String hdfs, boolean removeHeader, String ext) throws IOException {

		FileSystem fileSystem = getFileSystem();
		Path pathFolder = new Path(hdfs);
		FileStatus[] files = fileSystem.listStatus(pathFolder);

		List<String> filenames = new Vector<String>();

		if (files != null) {

			// filters by extension and sorts by filename
			for (FileStatus file : files) {
				if (!file.isDir() && !file.getPath().getName().startsWith("_")
						&& (ext == null || file.getPath().getName().endsWith(ext))) {
					filenames.add(file.getPath().toString());
				}
			}
			Collections.sort(filenames);

			Text line = new Text();

			boolean firstFile = true;

			for (String filename : filenames) {
				Path path = new Path(filename);

				FSDataInputStream in = fileSystem.open(path);

				LineReader reader = new LineReader(in);

				boolean header = true;
				while (reader.readLine(line, 1000) > 0) {

					if (removeHeader) {

						if (header) {
							if (firstFile) {
								out.write(line.toString().getBytes());
								firstFile = false;
							}
							header = false;
						} else {
							out.write('\n');
							out.write(line.toString().getBytes());
						}

					} else {

						if (header) {
							if (firstFile) {
								firstFile = false;
							} else {
								out.write('\n');
							}
							header = false;
						} else {
							out.write('\n');

						}
						out.write(line.toString().getBytes());
					}
				}
				line.clear();

				in.close();

			}

			out.close();
		}

	}

	public static void join(String local, String hdfs, int offset, String delimiter, String ext) {
		try {
			FileOutputStream out = new FileOutputStream(local);
			join(out, hdfs, offset, delimiter, ext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void joinAndGz(String local, String hdfs, int offset, String delimiter, String ext) {
		try {
			GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(local));
			join(out, hdfs, offset, delimiter, ext);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void join(OutputStream out, String hdfs, int offset, String delimiter, String ext) {

		try {

			FileSystem fileSystem = getFileSystem();
			Path pathFolder = new Path(hdfs);
			FileStatus[] files = fileSystem.listStatus(pathFolder);

			List<String> filenames = new Vector<String>();

			if (files != null) {

				// filters by extension and sorts by filename
				for (FileStatus file : files) {
					if (!file.isDir() && !file.getPath().getName().startsWith("_")
							&& (ext == null || file.getPath().getName().endsWith(ext))) {
						filenames.add(file.getPath().toString());
					}
				}
				Collections.sort(filenames);

				Text line = new Text();

				FSDataInputStream[] streams = new FSDataInputStream[filenames.size()];
				LineReader[] readers = new LineReader[filenames.size()];
				boolean[] empty = new boolean[filenames.size()];
				for (int i = 0; i < filenames.size(); i++) {
					Path path = new Path(filenames.get(i));
					streams[i] = fileSystem.open(path);
					readers[i] = new LineReader(streams[i]);
					empty[i] = false;
				}

				boolean end = false;
				boolean firstLine = true;

				while (!end) {

					if (firstLine) {
						firstLine = false;
					} else {
						out.write('\n');
					}

					boolean firstColumn = true;
					end = true;
					for (int i = 0; i < filenames.size(); i++) {

						if (!empty[i]) {

							boolean read = (readers[i].readLine(line, 1000000) > 0);

							if (read) {

								if (firstColumn) {

									out.write(line.toString().getBytes());
									firstColumn = false;

								} else {
									out.write(delimiter.getBytes());
									out.write(line.toString().getBytes());
								}

								end = false;

							} else {
								empty[i] = true;
								end = end && true;
							}

						}

					}

				}

				for (int i = 0; i < filenames.size(); i++) {
					streams[i].close();
					readers[i].close();
				}

				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setExecutable(String filename, boolean execute) {
		try {
			FileSystem fileSystem = getFileSystem();

			FsPermission other = fileSystem.getFileStatus(new Path(filename)).getPermission();
			FsPermission permission = new FsPermission(FsAction.ALL, other.getGroupAction(), other.getOtherAction());
			fileSystem.setPermission(new Path(filename), permission);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean canExecute(String filename) {
		try {
			FileSystem fileSystem = getFileSystem();

			FsPermission permission = fileSystem.getFileStatus(new Path(filename)).getPermission();
			return permission.getUserAction().implies(FsAction.EXECUTE);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean canExecute(Path path) {
		try {
			FileSystem fileSystem = getFileSystem();

			FsPermission permission = fileSystem.getFileStatus(path).getPermission();
			return permission.getUserAction().implies(FsAction.EXECUTE);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static DataInputStream open(String filename) throws IOException {
		FileSystem fileSystem = getFileSystem();
		Path path = new Path(filename);
		return fileSystem.open(path);
	}

	public static FSDataOutputStream create(String filename) throws IOException {
		FileSystem fileSystem = getFileSystem();
		Path path = new Path(filename);
		return fileSystem.create(path);
	}

	public static List<String> getFiles(String hdfs) throws IOException {

		return getFiles(hdfs, null);

	}

	public static List<String> getFiles(String hdfs, String ext) throws IOException {

		FileSystem fileSystem = getFileSystem();
		Path pathFolder = new Path(hdfs);
		FileStatus[] files = fileSystem.listStatus(pathFolder);

		List<String> filenames = new Vector<String>();

		if (files != null) {

			// filters by extension and sorts by filename
			for (FileStatus file : files) {
				if (!file.isDir() && !file.getPath().getName().startsWith("_")
						&& (ext == null || file.getPath().getName().endsWith(ext))) {
					filenames.add(file.getPath().toString());
				}
			}
			Collections.sort(filenames);

		}

		return filenames;

	}

	public static List<String> getDirectories(String hdfs) throws IOException {

		FileSystem fileSystem = getFileSystem();
		Path pathFolder = new Path(hdfs);
		FileStatus[] files = fileSystem.listStatus(pathFolder);

		List<String> filenames = new Vector<String>();

		if (files != null) {

			// filters by extension and sorts by filename
			for (FileStatus file : files) {
				if (file.isDir() && !file.getPath().getName().startsWith("_")) {
					filenames.add(file.getPath().toString());
				}
			}
			Collections.sort(filenames);

		}

		return filenames;

	}

	public static String makeAbsolute(String path) {

		try {
			FileSystem fileSystem = getFileSystem();

			String temp = "";
			if (fileSystem.getHomeDirectory().toString().startsWith("file:/")) {
				temp = path;
			} else {
				temp = fileSystem.getHomeDirectory().toString() + "/" + path;
			}

			temp = temp.replaceFirst("//([a-zA-Z\\-.\\d]*)(:(\\d*))?/", "///");
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static boolean isAbsolute(String path) {

		return path.startsWith("hdfs://");

	}

	// old functions from cloudgene
	public static void compress(String zipFile, String hdfs) {
		// Create a buffer for reading the files
		byte[] buf = new byte[1024];

		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

			FileSystem fileSystem = getFileSystem();
			Path pathFolder = new Path(hdfs);
			FileStatus[] files = fileSystem.listStatus(pathFolder);

			if (files != null) {
				for (FileStatus file : files) {
					Path path = file.getPath();
					if (!file.isDir() && !file.getPath().getName().startsWith("_")) {
						FSDataInputStream in = fileSystem.open(path);
						out.putNextEntry(new ZipEntry(path.getName()));

						// Transfer bytes from the file to the ZIP file
						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						// Complete the entry
						out.closeEntry();

						in.close();
					}
				}

				// Complete the ZIP file
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void compressAndMerge(String zipFile, String hdfs, boolean removeHeader) {

		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

			FileSystem fileSystem = getFileSystem();
			Path pathFolder = new Path(hdfs);
			FileStatus[] files = fileSystem.listStatus(pathFolder);

			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(pathFolder.getName() + ".txt"));

			Text line = new Text();

			if (files != null) {

				boolean firstFile = true;

				for (FileStatus file : files) {
					Path path = file.getPath();
					if (!file.isDir() && !file.getPath().getName().startsWith("_")
							&& !file.getPath().getName().startsWith(".pig_schema")) {
						FSDataInputStream in = fileSystem.open(path);

						LineReader reader = new LineReader(in);

						boolean header = true;
						while (reader.readLine(line, 1000) > 0) {

							if (removeHeader) {

								if (header) {
									if (firstFile) {
										out.write(line.toString().getBytes());
										firstFile = false;
									}
									header = false;
								} else {
									out.write('\n');
									out.write(line.toString().getBytes());
								}

							} else {

								if (header) {
									if (firstFile) {
										firstFile = false;
									} else {
										out.write('\n');
									}
									header = false;
								} else {
									out.write('\n');

								}
								out.write(line.toString().getBytes());
							}
						}
						line.clear();

						in.close();

					}
				}
				out.closeEntry();

				// Complete the ZIP file
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void exportDirectoryAndMerge(String folder, String name, String hdfs, boolean removeHeader) {

		try {
			FileOutputStream out = null;

			FileSystem fileSystem = getFileSystem();
			Path pathFolder = new Path(hdfs);
			FileStatus[] files = fileSystem.listStatus(pathFolder);

			out = new FileOutputStream(folder + "/" + name + ".txt");

			Text line = new Text();

			if (files != null) {
				boolean firstFile = true;
				for (FileStatus file : files) {
					Path path = file.getPath();
					if (!file.isDir() && !file.getPath().getName().startsWith("_")
							&& !file.getPath().getName().startsWith(".pig_schema")) {
						FSDataInputStream in = fileSystem.open(path);

						LineReader reader = new LineReader(in);

						boolean header = true;
						while (reader.readLine(line, 1000) > 0) {

							if (removeHeader) {

								if (header) {
									if (firstFile) {
										out.write(line.toString().getBytes());
										firstFile = false;
									}
									header = false;
								} else {
									out.write('\n');
									out.write(line.toString().getBytes());
								}

							} else {

								if (header) {
									if (firstFile) {
										firstFile = false;
									} else {
										out.write('\n');
									}
									header = false;
								} else {
									out.write('\n');

								}
								out.write(line.toString().getBytes());
							}
						}
						line.clear();

						in.close();
					}
				}

				out.close();

				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void exportDirectory(String folder, String name, String hdfs) {

		byte[] buf = new byte[1024];

		try {

			FileSystem fileSystem = getFileSystem();
			Path pathFolder = new Path(hdfs);
			FileStatus[] files = fileSystem.listStatus(pathFolder);
			System.out.println("export folder " + hdfs);
			if (files != null) {
				for (FileStatus file : files) {
					Path path = file.getPath();
					if (!file.isDirectory() && !file.getPath().getName().startsWith("_")) {
						FSDataInputStream in = fileSystem.open(path);
						FileOutputStream out = new FileOutputStream(folder + "/" + path.getName());
						System.out.println("  export file " + path);
						int len;
						while ((len = in.read(buf)) > 0) {
							out.write(buf, 0, len);
						}

						out.close();

						in.close();
					} else if (file.isDirectory()) {
						exportDirectory(folder, name, HdfsUtil.path(hdfs, path.getName()));
					}
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void exportFile(String folder, String hdfs) {

		byte[] buf = new byte[1024];

		try {

			FileSystem fileSystem = getFileSystem();

			Path path = new Path(hdfs);

			FSDataInputStream in = fileSystem.open(path);
			FileOutputStream out = new FileOutputStream(folder + "/" + path.getName());

			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			out.close();

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void compressFile(String folder, String hdfs) {

		byte[] buf = new byte[1024];

		try {

			FileSystem fileSystem = getFileSystem();

			Path path = new Path(hdfs);

			FSDataInputStream in = fileSystem.open(path);

			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(folder + "/" + path.getName() + ".zip"));

			out.putNextEntry(new ZipEntry(path.getName()));

			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			// Complete the entry
			out.closeEntry();

			out.close();

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean createDirectory(FileSystem fileSystem, String directory) {
		Path path = new Path(directory);
		try {
			fileSystem.mkdirs(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean createDirectory(String directory) {
		try {
			FileSystem fileSystem = getFileSystem();
			return createDirectory(fileSystem, directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean rename(FileSystem fileSystem, String oldPath, String newPath) {
		Path old = new Path(oldPath);
		Path newP = new Path(newPath);
		try {
			fileSystem.rename(old, newP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean rename(String oldPath, String newPath) {
		try {
			FileSystem fileSystem = getFileSystem();
			return rename(fileSystem, oldPath, newPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void checkOut(String hdfs, String filename) throws IOException {

		FileSystem fileSystem = getFileSystem();
		Path path = new Path(hdfs);

		if (fileSystem.isDirectory(path)) {

			// merge
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(filename));

			Path headerPath = new Path(hdfs + "/.pig_header");

			if (fileSystem.exists(headerPath)) {
				FSDataInputStream is = fileSystem.open(headerPath);
				LineReader reader = new LineReader(is);
				Text header = new Text();
				reader.readLine(header);
				reader.close();
				is.close();

				fos.writeBytes(header.toString() + "\n");
			}

			FileStatus[] files = fileSystem.listStatus(new Path(hdfs));

			for (FileStatus file : files) {
				if (!file.isDir() && !file.getPath().getName().startsWith(".")
						&& !file.getPath().getName().startsWith("_")) {

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
			System.out.println("Check out file done... (" + size + " bytes)");

		}

	}

}
