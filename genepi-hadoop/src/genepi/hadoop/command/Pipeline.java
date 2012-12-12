package genepi.hadoop.command;

import java.util.List;
import java.util.Vector;

public class Pipeline implements ICommand {

	private List<ICommand> commands;
	
	private String name;

	public Pipeline(String name) {
		commands = new Vector<ICommand>();
		this.name = name;
	}
	
	public void add(ICommand command){
		commands.add(command);
	}

	@Override
	public List<String> getInputs() {
		return commands.get(0).getInputs();
	}

	@Override
	public List<String> getOutputs() {
		return commands.get(commands.size() - 1).getOutputs();
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
		for (ICommand command : commands) {
			int result = command.execute();
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

}
