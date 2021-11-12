package genepi.command;

import java.util.List;

public interface ICommand {

	public String getName();
	
	public List<String> getInputs();

	public List<String> getOutputs();

	public String getSignature();

	public int execute();

}
