# Genepi-Hadoop Library

Genepi-Hadoop Library contains several utility classes for Apache Hadoop.

## Jobs

### Hadoop Job Template

	public class MyJob extends HadoopJob {
	
		public MyJob() {
	
			super("my job");
			//configuration:
			getConfiguration().set("key", "value");
		}
	
		@Override
		public void setupJob(Job job) {
	
			job.setInputFormatClass(TextInputFormat.class);
	
			job.setMapperClass(MyMapper.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
	
			job.setReducerClass(MyReducer.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(Text.class);
		}
	
	    //optional:

		@Override
		public void cleanupJob(Job job) {
	
		}
	
		@Override
		public void before() {
		
		}
	
		@Override
		public void after() {
		
		}
	
	}

### Job Execution

	MyJob job = new MyJob();
	job.setInput(hdfsInput);
	job.setOutput(hdfsOutput);

	boolean result = job.execute();

	if (!result){
		//error
		System.exit(-1);
	}

### Job Configuration File

The job class loads all properties which are set in the file "job.config" and distributes them using the PreferenceStore class.

job.config:

	property1=my property
	property2=my property2
	property3=my property3

Accessing a property in your Mapper class:

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {

		PreferenceStore store = new PreferenceStore(context.getConfiguration());
		String property1 = store.getString("property1");
		
	}



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


#### Using the cache in your HadoopJob

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


#### Caching in files in your Mapper/Reducer

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


#### Using cached files

		FolderCache.getInstance().load(CACHE);
		String directory = FolderCache.getInstance().getCachedDirectory(
				"unique-signature");

		if (directory == null) {

			//files are not cached

		} else {

			//files are cached in directory (hdfs path!)

		}
