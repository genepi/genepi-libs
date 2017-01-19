package genepi.hadoop.log;

import genepi.hadoop.HdfsUtil;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class Log {

	private Context context;
	private String filename;
	private FSDataOutputStream out;

	public Log(Context context) {
		this.context = context;
		this.filename = HdfsUtil.path(LogCollector.LOG_DIRECTORY, context
				.getJobID().toString(), context.getTaskAttemptID().toString());

		Configuration conf = HdfsUtil.getConfiguration();
		try {

			FileSystem fileSystem = FileSystem.get(conf);
			out = fileSystem.create(new Path(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void writeLine(String message) {
		String line = "[" + context.getTaskAttemptID() + "] " + message + "\n";

		try {

			out.write(line.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop(String message, Exception e) throws InterruptedException {
		writeLine("ERROR  " + message + "\n" + e);
		throw new InterruptedException(message);
	}

	public void stop(String message, String details)
			throws InterruptedException {
		writeLine("ERROR  " + message + "\n" + details);
		throw new InterruptedException(message);
	}

	public void error(String message, Exception e) {
		writeLine("ERROR  " + message + "\n" + e);
	}

	public void error(String message, String details) {
		writeLine("ERROR  " + message + "\n" + details);
	}

	public void info(String message) {
		writeLine("INFO  " + message);
	}

	public void close() {
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
