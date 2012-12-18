package genepi.hadoop.tests.foldercache;

import genepi.hadoop.HadoopJob;
import genepi.hadoop.HdfsUtil;
import genepi.hadoop.cache.FolderCache;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class FolderCachetest extends HadoopJob {

	public static String CACHE_FOLDER = "/home/lukas/hdfs/luki/folder-cache";

	public FolderCachetest(String name) {
		super(name);
	}

	@Override
	public void setupJob(Job job) {

		FolderCache.getInstance().load(CACHE_FOLDER);
		
		job.setInputFormatClass(TextInputFormat.class);

		job.setMapperClass(FolderCacheMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(FolderCacheReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

	}

	@Override
	public void cleanupJob(Job job) {
		FolderCache.getInstance().updateAndSave(job);
	}

	public static void main(String[] args) {

		FolderCache.getInstance().load(CACHE_FOLDER);
		String directory = FolderCache.getInstance().getCachedDirectory(
				"my-sig");

		if (directory == null) {

			FolderCachetest test = new FolderCachetest("Folder Cache test");
			test.setInput("/home/lukas/hdfs/luki/cache-input");
			HdfsUtil.delete("/home/lukas/hdfs/luki/cache-output");
			test.setOutput("/home/lukas/hdfs/luki/cache-output");
			test.execute();

		} else {

			System.out.println("Files are cached in " + directory);

		}
	}

}
