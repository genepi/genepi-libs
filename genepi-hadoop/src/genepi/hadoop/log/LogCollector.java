package genepi.hadoop.log;

import genepi.hadoop.HdfsUtil;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.apache.hadoop.mapreduce.Job;

public class LogCollector {

	private Job job;
	private String folder;

	public static String LOG_DIRECTORY = "mylogs";

	public LogCollector(Job job) {
		this.job = job;
		this.folder = HdfsUtil.path(LOG_DIRECTORY, job.getJobID().toString());
		load(folder);
	}

	protected void load(String folder) {

	}

	public void save(String filename) throws IOException {
		HdfsUtil.merge(filename, folder, false);
	}

	public boolean hasErrors() {
		return false;
	}

	public List<String> getEntries() {
		return new Vector<String>();
	}

}
