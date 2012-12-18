#Hadoop-Genepi Library

## Jobs

### Simple Hadoop Job

### Job Configuration File

### Distributed HashMap


## Hdfs Utilities

## Commands

### Simple Command

	Command command = new Command("/usr/bin/ls");
	command.setParams(param1, param2, param3, ...);
	command.saveStdOut("command.out"); //save command output (optional)
	command.setSilent(true); //no output on stdout (optional)
	command.execute();

### Group of Commands

	Command command1 = new Command("/usr/bin/ls");
	command1.setParams(param1, param2, param3, ...);

	Command command2 = new Command("/usr/bin/ls");
	command2.setParams(param1, param2, param3, ...);
	
	CommandGroup group = new CommandGroup("my group");
	group.add(command1);
	group.add(command2);
	group.execute();


### Command Pipeline

Command 2 reads stdout from command 1:

	PipedCommand command1 = new PipedCommand("/usr/bin/ls");
	command1.setParams(foldername);

	PipedCommand command2 = new Command("/usr/bin/grep");
	command2.setParams(filename);
	
	Pipeline pipeline = new Pipeline("my pipeline");
	pipeline.add(command1);
	pipeline.add(command2);
	pipeline.execute();


### R-Scripts


## Cache

### Caching in Commands


### Caching files and folders:


#### HadoopJob:

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


#### Mapper or Reducer:

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


#### Using cached files:

		FolderCache.getInstance().load(CACHE);
		String directory = FolderCache.getInstance().getCachedDirectory(
				"unique-signature");

		if (directory == null) {

			//files are cached in directory (hdfs path!)

		} else {

			//files are not cached

		}
