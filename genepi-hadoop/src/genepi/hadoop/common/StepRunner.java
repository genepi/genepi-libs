package genepi.hadoop.common;

public class StepRunner {

	public static boolean run(String[] args, WorkflowStep step) throws Exception {

		CommandLineContext context = new CommandLineContext(args);
		step.setup(context);
		return step.run(context);

	}

}
