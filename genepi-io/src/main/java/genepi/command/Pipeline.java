package genepi.command;

import java.util.List;
import java.util.Vector;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Pipeline implements ICommand {

	protected static final Log log = LogFactory.getLog(Pipeline.class);

	private List<PipedCommand> commands;

	private String name;

	private List<String> inputs = new Vector<String>();
	
	private List<String> outputs = new Vector<String>();
	
	public Pipeline(String name) {
		commands = new Vector<PipedCommand>();
		this.name = name;
	}

	public void add(PipedCommand command) {
		if (!commands.isEmpty()) {
			PipedCommand prevStep = commands.get(commands.size() - 1);
			command.readFrom(prevStep);
		}
		inputs.addAll(command.getInputs());
		outputs.addAll(command.getOutputs());
		commands.add(command);
	}

	@Override
	public List<String> getInputs() {
		return inputs;
	}

	@Override
	public List<String> getOutputs() {
		return outputs;
	}

	@Override
	public String getSignature() {
		String signature = "";
		for (PipedCommand command : commands) {
			signature += command.getSignature();
		}
		return DigestUtils.md5Hex(signature);
	}

	@Override
	public int execute() {
		log.info("Running pipeline " + name + "...");
		PipedCommand lastStep = commands.get(commands.size() - 1);
		int result = lastStep.execute();
		if (result == 0) {
			log.info("Running pipeline " + name + " successful.");
		} else {
			log.info("Running pipeline " + name + " failed.");
		}
		return result;
	}

	@Override
	public String getName() {
		return name;
	}

}
