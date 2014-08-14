package genepi.hadoop;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TaskCompletionEvent;
import org.apache.hadoop.mapred.TaskLog;
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

	private Job job;

	private String taskLocalData = "/temp/dist";

	private Class myClass = null;

	private String jar = null;

	private RunningJob runningJob = null;

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

	}

	private String getFolder(Class clazz) {
		return new File(clazz.getProtectionDomain().getCodeSource()
				.getLocation().getPath()).getParent();
	}

	protected void readConfigFile() {

		String folder = null;

		if (myClass != null) {
			folder = getFolder(myClass);
		} else {
			folder = getFolder(HadoopJob.class);
		}

		File file = new File(folder + "/" + CONFIG_FILE);
		if (file.exists()) {
			log.info("Loading distributed configuration file " + folder + "/"
					+ CONFIG_FILE + "...");
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

	protected void setupDistributedCache(CacheStore cache) throws IOException {

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

	public void setJarByClass(Class clazz) {
		myClass = clazz;
	}

	public void setJar(String jar) {
		this.jar = jar;
	}

	public boolean execute() {

		readConfigFile();

		log.info("Setting up Distributed Cache...");
		CacheStore cacheStore = new CacheStore(configuration);
		try {
			setupDistributedCache(cacheStore);
		} catch (Exception e) {
			log.error("Set up Distributed Cache failed.", e);
			return false;
		}

		if (jar != null) {
			String temp = HdfsUtil.path("test", "test.jar");
			HdfsUtil.put(jar, temp);
			try {
				DistributedCache.addFileToClassPath(new Path(
						"hdfs://localhost:9000/user/lukas/test/test.jar"),
						configuration);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		log.info("Running Preprocessing...");
		before();

		job = null;
		try {
			job = new Job(configuration, name);

			if (myClass != null) {

				job.setJarByClass(myClass);
			} else {

				job.setJarByClass(HadoopJob.class);

			}

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
			// job.waitForCompletion(true);

			job.submit();

			JobClient jobClient = new JobClient(getConfiguration());

			while (!job.isComplete()) {
				if (runningJob == null) {
					if (job.getJobID() != null) {
						String id = job.getJobID().toString();

						if (jobClient != null) {
							runningJob = jobClient.getJob(id);
							System.out.println("Found running job!!");
						}
					}
				}
				Thread.sleep(1000);
			}

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
		} catch (InterruptedException e) {
			log.error("Execution canceld by user.");
			cleanupJob(job);
			return false;

		} catch (Exception e) {
			log.error("Execution failed.", e);
			cleanupJob(job);
			return false;
		}

	}

	public void kill() throws IOException {
		job.killJob();
	}

	public String getJobId() {
		if (job != null) {
			return job.getJobID().toString();
		} else {
			return null;
		}
	}

	public void downloadFailedLogs(String folder) {

		log.info("Downloading events...");

		List<TaskCompletionEvent> completionEvents = new LinkedList<TaskCompletionEvent>();
		try {
			while (true) {
				TaskCompletionEvent[] bunchOfEvents;
				bunchOfEvents = runningJob
						.getTaskCompletionEvents(completionEvents.size());
				if (bunchOfEvents == null || bunchOfEvents.length == 0) {
					break;
				}
				completionEvents.addAll(Arrays.asList(bunchOfEvents));

			}

		} catch (Exception e) {
			log.error("Downloading events failed.", e);
			return;
		}

		log.info("Downloaded " + completionEvents.size() + " events.");

		log.info("Downloading " + completionEvents.size() + " log files...");
		for (TaskCompletionEvent taskCompletionEvent : completionEvents) {

			StringBuilder logURL = new StringBuilder(
					taskCompletionEvent.getTaskTrackerHttp());
			logURL.append("/tasklog?attemptid=");
			logURL.append(taskCompletionEvent.getTaskAttemptId().toString());
			logURL.append("&plaintext=true");
			logURL.append("&filter=" + TaskLog.LogName.STDOUT);

			log.info("Downloading " + logURL + "...");

			try {
				URL url = new URL(logURL.toString());
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				BufferedInputStream in = new BufferedInputStream(
						conn.getInputStream());

				String local = folder + "/"
						+ taskCompletionEvent.getStatus().toString() + "_"
						+ taskCompletionEvent.getTaskAttemptId().toString()
						+ "_stdout.txt";

				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(local));
				IOUtils.copy(in, out);

				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			} catch (Exception e) {
				log.error("Downloading log files failed.", e);
				return;
			}

			logURL = new StringBuilder(taskCompletionEvent.getTaskTrackerHttp());
			logURL.append("/tasklog?attemptid=");
			logURL.append(taskCompletionEvent.getTaskAttemptId().toString());
			logURL.append("&plaintext=true");
			logURL.append("&filter=" + TaskLog.LogName.STDERR);

			log.info("Downloading " + logURL + "...");

			try {
				URL url = new URL(logURL.toString());
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				BufferedInputStream in = new BufferedInputStream(
						conn.getInputStream());

				String local = folder + "/"
						+ taskCompletionEvent.getStatus().toString() + "_"
						+ taskCompletionEvent.getTaskAttemptId().toString()
						+ "_stderr.txt";

				BufferedOutputStream out = new BufferedOutputStream(
						new FileOutputStream(local));
				IOUtils.copy(in, out);

				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			} catch (Exception e) {
				log.error("Downloading log files failed.", e);
				return;
			}

		}

		log.info("Downloading log files successful.");

	}
}
