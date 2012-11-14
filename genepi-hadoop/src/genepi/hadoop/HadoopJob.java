package genepi.hadoop;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;

public abstract class HadoopJob extends Job {

	public static String CONFIG_FILE = "job.config";

	public HadoopJob(String name) throws IOException {
		super(new Configuration(), name);

		getConfiguration().set("mapred.child.java.opts", "-Xmx4000M");
		getConfiguration().set("mapred.task.timeout", "0");

		File file = new File(CONFIG_FILE);
		if (file.exists()) {

			PreferenceStore preferenceStore = new PreferenceStore(file);
			preferenceStore.write(getConfiguration());

		}

	}

	public void before() {

	}

	public void after() {

	}

	public boolean execute() {
		before();
		try {
			waitForCompletion(true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		boolean successful;
		try {
			successful = isSuccessful();
			if (successful) {
				after();
			}
			return successful;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

}
