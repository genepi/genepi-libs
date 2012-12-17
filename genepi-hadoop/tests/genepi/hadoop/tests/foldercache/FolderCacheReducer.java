package genepi.hadoop.tests.foldercache;

import genepi.hadoop.cache.FolderCache;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FolderCacheReducer extends Reducer<Text, Text, Text, Text> {

	protected void setup(Context context) throws IOException,
			InterruptedException {

		FolderCache.getInstance().load(FolderCachetest.CACHE_FOLDER);

	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {

		FolderCache.getInstance().save(context);

	}

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {

		// write to locale file
		String localFile = "local-file-"
				+ context.getTaskAttemptID().toString();

		try {
			FileWriter fstream = new FileWriter(localFile);
			BufferedWriter out = new BufferedWriter(fstream);
			for (Text value : values) {
				context.write(key, value);
				out.write(value.toString());
				out.write("\n");
			}

			out.close();
			fstream.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		FolderCache.getInstance().cacheFile("my-sig", localFile);

	}

}
