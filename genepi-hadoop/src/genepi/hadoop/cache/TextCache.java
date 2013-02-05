package genepi.hadoop.cache;

import genepi.hadoop.HdfsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class TextCache {

	protected static final Log log = LogFactory.getLog(TextCache.class);

	private String cacheDirectory;

	private List<TextCacheEntry> entries;

	private List<TextCacheEntry> updates;

	public static TextCache instance = null;

	private boolean loaded = false;

	public static TextCache getInstance() {
		if (instance == null) {
			instance = new TextCache();
		}
		return instance;
	}

	private TextCache() {
		entries = new Vector<TextCacheEntry>();
		updates = new Vector<TextCacheEntry>();
	}

	public void load(String cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
		entries = new Vector<TextCacheEntry>();
		updates = new Vector<TextCacheEntry>();

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
						TextCacheEntry entry = TextCacheEntry.parse(line
								.toString());
						entries.add(entry);
					}
				}
				reader.close();

				loaded = true;

			} else {

				log.info("Cache index file " + indexFile + " not present.");

				loaded = true;

			}

		} catch (Exception e) {

			log.error("Loading Cache index file failed.", e);

			loaded = false;
		}

	}

	public Map<String, String> get(String signature) {

		Map<String, String> result = new HashMap<String, String>();

		if (loaded) {

			for (TextCacheEntry cacheEntry : entries) {
				if (cacheEntry.getSignature().equals(signature)) {
					log.info(signature + " is cached.");
					result.put(cacheEntry.getKey(), cacheEntry.getValue());
				}
			}
			log.info(signature + " is not cached.");

			if (result.isEmpty()) {

				return null;

			} else {

				return result;

			}

		} else {

			throw new RuntimeException("Cache is not loaded yet.");

		}

	}

	public void cacheKeyValue(String signature, String key, String value) {

		if (loaded) {

			TextCacheEntry entry = new TextCacheEntry(signature);
			entry.setKey(key);
			entry.setValue(value);

			log.info("Put " + signature + " into cache.");
			entries.add(entry);
			updates.add(entry);

			log.info("Storing " + signature + "(" + key + ", " + value
					+ ")  file into cache...");

		} else {

			throw new RuntimeException("Cache is not loaded yet.");

		}

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

		if (loaded) {

			if (!updates.isEmpty()) {

				log.info("Write " + updates.size()
						+ " changes into cache index file...");

				Configuration conf = new Configuration();
				try {

					FileSystem fileSystem = FileSystem.get(conf);
					FSDataOutputStream out = fileSystem
							.create(new Path(target));

					for (TextCacheEntry entry : updates) {
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

		} else {

			throw new RuntimeException("Cache is not loaded yet.");

		}

	}

	protected void saveAll(String target) {

		if (loaded) {

			if (!updates.isEmpty()) {

				log.info("Write " + updates.size()
						+ " changes into cache index file...");

				Configuration conf = new Configuration();
				try {

					FileSystem fileSystem = FileSystem.get(conf);
					FSDataOutputStream out = fileSystem
							.create(new Path(target));

					for (TextCacheEntry entry : entries) {
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

		} else {

			throw new RuntimeException("Cache is not loaded yet.");

		}

	}

	public void updateAndSave(Job job) {

		log.info("Update cache...");

		if (loaded) {

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

								TextCacheEntry entry = TextCacheEntry
										.parse(line.toString());

								entries.add(entry);
								updates.add(entry);

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

		} else {

			throw new RuntimeException("Cache is not loaded yet.");

		}

	}

	public void clear(String directory) {
		log.info("Clear cache...");

		if (loaded) {

			HdfsUtil.delete(directory);
			log.info("Cache is empty.");

		} else {

			throw new RuntimeException("Cache is not loaded yet.");

		}
	}

}
