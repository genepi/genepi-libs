package genepi.hadoop.cache;

import genepi.hadoop.HdfsUtil;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.LineReader;

public class FolderCache {

	protected static final Log log = LogFactory.getLog(FolderCache.class);

	private String cacheDirectory;

	private List<FolderCacheEntry> entries;

	private List<FolderCacheEntry> updates;

	public static FolderCache instance = null;

	public static FolderCache getInstance() {
		if (instance == null) {
			instance = new FolderCache();
		}
		return instance;
	}

	private FolderCache() {
		entries = new Vector<FolderCacheEntry>();
		updates = new Vector<FolderCacheEntry>();
	}

	public void load(String cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
		entries = new Vector<FolderCacheEntry>();
		updates = new Vector<FolderCacheEntry>();

		Configuration conf = new Configuration();
		try {

			FileSystem fileSystem = FileSystem.get(conf);
			Path indexFile = new Path(HdfsUtil.path(cacheDirectory, "index"));
			Text line = new Text();

			if (fileSystem.exists(indexFile)) {

				log.info("Loading cache index file " + indexFile + "...");

				FSDataInputStream is = fileSystem.open(indexFile);
				LineReader reader = new LineReader(is);
				while (reader.readLine(line, 500) > 0) {
					if (line.toString().length() > 0) {
						FolderCacheEntry entry = FolderCacheEntry.parse(line
								.toString());
						entries.add(entry);
					}
				}
				reader.close();

			} else {

				log.info("Cache index file " + indexFile + " not present.");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getCachedDirectory(String signature) {
		for (FolderCacheEntry cacheEntry : entries) {
			if (cacheEntry.getSignature().equals(signature)) {
				log.info(signature + " is cached.");
				return cacheEntry.getFolder();
			}
		}
		log.info(signature + " is not cached.");
		return null;

	}

	public void cacheFile(String signature, String filename) {

		String folder = HdfsUtil.path(cacheDirectory, "data", signature);

		FolderCacheEntry entry = new FolderCacheEntry(signature);
		entry.setFolder(folder);

		log.info("Put " + signature + " into cache.");
		entries.add(entry);
		updates.add(entry);

		log.info("Storing " + filename + " file into cache...");

		String name =  CommandCacheEntry.getMd5Hex(filename);
		String target = HdfsUtil.path(folder, name);
		HdfsUtil.put(filename, target);

	}

	public void save(Mapper.Context context) {

		String job = context.getJobID().toString();
		String name = context.getTaskAttemptID().toString();
		String target = HdfsUtil.path(cacheDirectory, "meta", job, name);

		save(target);

	}

	public void save(Reducer.Context context) {

		String job = context.getJobID().toString();
		String name = context.getTaskAttemptID().toString();
		String target = HdfsUtil.path(cacheDirectory, "meta", job, name);

		save(target);

	}

	public void save(String target) {

		if (!updates.isEmpty()) {

			log.info("Write " + updates.size()
					+ " changes into cache index file...");

			Configuration conf = new Configuration();
			try {

				FileSystem fileSystem = FileSystem.get(conf);
				FSDataOutputStream out = fileSystem.create(new Path(target));

				for (FolderCacheEntry entry : updates) {
					out.write(entry.toString().getBytes());
					out.write('\n');
				}

				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			log.info("No change in cache index file.");

		}

	}

	protected void saveAll(String target) {

		if (!updates.isEmpty()) {

			log.info("Write " + updates.size()
					+ " changes into cache index file...");

			Configuration conf = new Configuration();
			try {

				FileSystem fileSystem = FileSystem.get(conf);
				FSDataOutputStream out = fileSystem.create(new Path(target));

				for (FolderCacheEntry entry : entries) {
					out.write(entry.toString().getBytes());
					out.write('\n');
				}

				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			log.info("No change in cache index file.");

		}

	}

	public void updateAndSave(Job job) {

		log.info("Update cache...");

		String name = job.getJobID().toString();
		String directory = HdfsUtil.path(cacheDirectory, "meta", name);

		Configuration conf = new Configuration();
		try {
			FileSystem fileSystem = FileSystem.get(conf);
			Path splitDirectory = new Path(directory);

			FileStatus[] files = fileSystem.listStatus(splitDirectory);
			if (files != null) {
				Text line = new Text();
				for (FileStatus file : files) {

					FSDataInputStream is = fileSystem.open(file.getPath());
					LineReader reader = new LineReader(is);
					while (reader.readLine(line, 500) > 0) {
						if (line.toString().length() > 0) {

							FolderCacheEntry entry = FolderCacheEntry
									.parse(line.toString());
							if (getCachedDirectory(entry.getSignature()) == null) {
								entries.add(entry);
								updates.add(entry);
							}

						}
					}
					reader.close();

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String target = HdfsUtil.path(cacheDirectory, "index");
		saveAll(target);

	}
	
	public void clear(String directory) {
		log.info("Clear cache...");
		HdfsUtil.delete(directory);
		log.info("Cache is empty.");
	}

}
