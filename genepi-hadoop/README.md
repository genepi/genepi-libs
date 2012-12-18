# Cache


## FolderCache Example:


### HadoopJob:

	@Override
	public void setupJob(Job job) {
		//load cache index file (CACHE is a hdfs folder)
		FolderCache.getInstance().load(CACHE);
	}

	@Override
	public void cleanupJob(Job job) {
		//update and save cache index file
		FolderCache.getInstance().updateAndSave(job);
	}


### Mapper or Reducer:

	protected void setup(Context context) throws IOException,
			InterruptedException {
			//load cache index file (CACHE is a hdfs folder)
		FolderCache.getInstance().load(CACHE);
	}

	@Override
	protected void cleanup(Context context) throws IOException,
			InterruptedException {
		FolderCache.getInstance().save(context);
	}

	@Override
	protected void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		//cache local file (no hdfs file!)
		FolderCache.getInstance().cacheFile("unique-signature", filename);
	}


### Using cached files:

		FolderCache.getInstance().load(CACHE);
		String directory = FolderCache.getInstance().getCachedDirectory(
				"unique-signature");

		if (directory == null) {

			//files are cached in directory (hdfs path!)

		} else {

			//files are not cached

		}
