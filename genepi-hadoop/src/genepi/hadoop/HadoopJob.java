package genepi.hadoop;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public abstract class HadoopJob {

	private static final Log log = LogFactory.getLog(HadoopJob.class);

	public static final String CONFIG_FILE = "job.config";

	private String output;

	private String input;

	private String name;

	private Configuration configuration;

	public HadoopJob(String name) {

		this.name = name;
		configuration = new Configuration();

		configuration.set("mapred.child.java.opts", "-Xmx4000M");
		configuration.set("mapred.task.timeout", "0");

		File file = new File(CONFIG_FILE);
		if (file.exists()) {

			PreferenceStore preferenceStore = new PreferenceStore(file);
			preferenceStore.write(configuration);

		}

	}

	protected void setupDistributedCache(CacheStore cache) {

	}

	public abstract void setupJob(Job job);

	public Configuration getConfiguration() {
		return configuration;
	}

	public void set(String name, int value) {
		configuration.setInt(name, value);
	}

	public void set(String name, String value) {
		configuration.set(name, value);
	}

	public void set(String name, boolean value) {
		configuration.setBoolean(name, value);
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getInput() {
		return input;
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

	public boolean execute() {

		log.info("Setting up Distributed Cache...");
		CacheStore cacheStore = new CacheStore(configuration);
		setupDistributedCache(cacheStore);

		Job job = null;
		try {
			job = new Job(configuration, name);
			job.setJarByClass(HadoopJob.class);
			log.info("Creating Job " + name + "...");
			setupJob(job);

			log.info("Running Preprocessing...");
			before();

			try {
				log.info("Input Path: " + input);
				FileInputFormat.addInputPath(job, new Path(input));
			} catch (IOException e) {
				log.error("Errors setting Input Input Path " + input, e);
			}
			log.info("Output Path: " + output);
			FileOutputFormat.setOutputPath(job, new Path(output));

			log.info("Running Job...");
			job.waitForCompletion(true);

			boolean result = job.isSuccessful();

			if (result) {
				log.info("Execution successful.");
				log.info("Running Postprocessing...");
				after();
				return true;
			} else {
				log.info("Execution failed.");
				return false;
			}

		} catch (Exception e) {
			log.error("Execution failed.", e);
			return false;
		}

	}

}
