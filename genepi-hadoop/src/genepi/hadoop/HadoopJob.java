package genepi.hadoop;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public abstract class HadoopJob {

	protected static final Log log = LogFactory.getLog(HadoopJob.class);

	public static final String CONFIG_FILE = "job.config";

	private String output;

	private String[] inputs;

	private String name;

	private Configuration configuration;

	private FileSystem fileSystem;

	private boolean canSet = false;

	private String taskLocalData = "/temp/dist";

	public HadoopJob(String name) {

		this.name = name;
		configuration = new Configuration();

		configuration.set("mapred.child.java.opts", "-Xmx4000M");
		configuration.set("mapred.task.timeout", "0");

		try {
			fileSystem = FileSystem.get(configuration);
		} catch (IOException e) {
			log.error("Creating FileSystem class failed.", e);
		}

		canSet = true;

		readConfigFile();

	}

	protected void readConfigFile() {
		File file = new File(CONFIG_FILE);
		if (file.exists()) {
			log.info("Loading distributed configuration file " + CONFIG_FILE
					+ "...");
			PreferenceStore preferenceStore = new PreferenceStore(file);
			preferenceStore.write(configuration);
			for (Object key : preferenceStore.getKeys()) {
				log.info("  " + key + ": "
						+ preferenceStore.getString(key.toString()));
			}

		} else {

			log.info("No distributed configuration file (" + CONFIG_FILE
					+ ") available.");

		}
	}

	protected void setupDistributedCache(CacheStore cache) {

	}

	public abstract void setupJob(Job job);

	public Configuration getConfiguration() {
		return configuration;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public void set(String name, int value) {
		if (canSet) {
			configuration.setInt(name, value);
		} else {
			new RuntimeException("Property '" + name
					+ "' couldn't be set. Configuration is looked.");
		}
	}

	public void set(String name, String value) {
		if (canSet) {
			configuration.set(name, value);
		} else {
			new RuntimeException("Property '" + name
					+ "' couldn't be set. Configuration is looked.");
		}
	}

	public void set(String name, boolean value) {
		if (canSet) {
			configuration.setBoolean(name, value);
		} else {
			new RuntimeException("Property '" + name
					+ "' couldn't be set. Configuration is looked.");
		}
	}

	public void setInput(String... inputs) {
		this.inputs = inputs;
	}

	public String[] getInputs() {
		return inputs;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public void before() {

	}

	public void after() {

	}

	public void cleanupJob(Job job) {

	}

	public boolean execute() {

		log.info("Setting up Distributed Cache...");
		CacheStore cacheStore = new CacheStore(configuration);
		setupDistributedCache(cacheStore);

		log.info("Running Preprocessing...");
		before();

		Job job = null;
		try {
			job = new Job(configuration, name);
			job.setJarByClass(HadoopJob.class);
			log.info("Creating Job " + name + "...");

			// look configuration
			canSet = false;

			setupJob(job);

			try {
				for (String input : inputs) {
					log.info("  Input Path: " + input);
				}
				for (String input : inputs) {
					FileInputFormat.addInputPath(job, new Path(input));
				}
			} catch (IOException e) {
				log.error("  Errors setting Input Path: ", e);
			}
			log.info("  Output Path: " + output);
			FileOutputFormat.setOutputPath(job, new Path(output));

			log.info("Running Job...");
			job.waitForCompletion(true);

			boolean result = job.isSuccessful();

			if (result) {
				log.info("Execution successful.");
				log.info("Running Postprocessing...");
				after();
				cleanupJob(job);
				return true;
			} else {
				log.info("Execution failed.");
				cleanupJob(job);
				return false;
			}

		} catch (Exception e) {
			log.error("Execution failed.", e);
			cleanupJob(job);
			return false;
		}

	}

}
