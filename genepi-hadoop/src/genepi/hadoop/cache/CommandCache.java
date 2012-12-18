package genepi.hadoop.cache;

import genepi.hadoop.HdfsUtil;
import genepi.hadoop.command.ICommand;

import java.io.IOException;
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

public class CommandCache {

	protected static final Log log = LogFactory.getLog(CommandCache.class);

	private String cacheDirectory;

	private List<CommandCacheEntry> entries;

	private List<CommandCacheEntry> updates;

	public static CommandCache instance = null;

	public static CommandCache getInstance() {
		if (instance == null) {
			instance = new CommandCache();
		}
		return instance;
	}

	private CommandCache() {
		entries = new Vector<CommandCacheEntry>();
		updates = new Vector<CommandCacheEntry>();
	}

	public void load(String cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
		entries = new Vector<CommandCacheEntry>();
		updates = new Vector<CommandCacheEntry>();

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
						CommandCacheEntry entry = CommandCacheEntry.parse(line
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

	public boolean isCached(ICommand command) {
		CommandCacheEntry entry = new CommandCacheEntry(command);
		return isCached(entry);
	}

	protected boolean isCached(CommandCacheEntry entry) {
		for (CommandCacheEntry cacheEntry : entries) {
			if (cacheEntry.isEqual(entry)) {
				if (entry.getCommand() != null) {
					log.info(entry.getName() + " is cached.");
				}
				return true;
			}
		}
		if (entry.getCommand() != null) {
			log.info(entry.getName() + " is not cached.");
		}
		return false;
	}

	public boolean checkOut(ICommand command) {
		CommandCacheEntry entry = new CommandCacheEntry(command);
		for (CommandCacheEntry cacheEntry : entries) {
			if (cacheEntry.isEqual(entry)) {
				// read files from hdfs cache
				log.info("Loading " + cacheEntry.getOutputFiles().length
						+ " file/s from cache...");

				for (int i = 0; i < cacheEntry.getOutputFiles().length; i++) {
					try {
						HdfsUtil.get(cacheEntry.getOutputFiles()[i], command
								.getOutputs().get(i));
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
				}

			}
		}
		return true;

	}

	public void cache(ICommand command) {

		CommandCacheEntry entry = new CommandCacheEntry(command);
		cache(entry);
	}

	protected void cache(CommandCacheEntry entry) {

		ICommand command = entry.getCommand();

		log.info("Put " + entry.getName() + " into cache.");
		entries.add(entry);
		updates.add(entry);

		log.info("Storing " + command.getOutputs().size()
				+ " file/s into cache...");

		// put output files into hdfs
		for (int i = 0; i < command.getOutputs().size(); i++) {
			String outputFile = command.getOutputs().get(i);
			String name = CommandCacheEntry.getMd5Hex(outputFile);
			String target = HdfsUtil.path(cacheDirectory, "data", name);
			HdfsUtil.put(outputFile, target);
			entry.getOutputFiles()[i] = target;
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

		if (!updates.isEmpty()) {

			log.info("Write " + updates.size()
					+ " changes into cache index file...");

			Configuration conf = new Configuration();
			try {

				FileSystem fileSystem = FileSystem.get(conf);
				FSDataOutputStream out = fileSystem.create(new Path(target));

				for (CommandCacheEntry entry : updates) {
					out.write(entry.toString().getBytes());
					out.write('\n');
				}

				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			log.info("No changes on cache.");

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

				for (CommandCacheEntry entry : entries) {
					out.write(entry.toString().getBytes());
					out.write('\n');
				}

				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			log.info("No changes on cache.");

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

							CommandCacheEntry entry = CommandCacheEntry
									.parse(line.toString());
							if (!isCached(entry)) {
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
