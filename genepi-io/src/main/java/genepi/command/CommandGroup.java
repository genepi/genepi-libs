package genepi.command;

import java.util.List;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommandGroup implements ICommand {

	protected static final Log log = LogFactory.getLog(CommandGroup.class);

	private List<ICommand> commands;

	private List<String> inputs = new Vector<String>();

	private List<String> outputs = new Vector<String>();

	private String name;

	public CommandGroup(String name) {
		commands = new Vector<ICommand>();
		this.name = name;
	}

	public void add(ICommand command) {
		commands.add(command);
		inputs.addAll(command.getInputs());
		outputs.addAll(command.getOutputs());
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
		for (ICommand command : commands) {
			signature += command.getSignature();
		}
		return org.apache.commons.codec.digest.DigestUtils.md5Hex(signature);
	}

	@Override
	public int execute() {
		log.info("Running command group " + name + "...");
		for (ICommand command : commands) {
			log.info("  Running command " + command + "...");
			int result = command.execute();
			if (result != 0) {
				log.info("Running command group " + name + " failed.");
				return result;
			}
		}
		log.info("Running command group " + name + " successful.");
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

}
