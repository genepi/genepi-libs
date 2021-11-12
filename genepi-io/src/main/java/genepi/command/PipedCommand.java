package genepi.command;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.codec.digest.DigestUtils;

public class PipedCommand {

	protected String cmd;

	private String[] params;

	private boolean silent = false;

	private boolean deleteInput = false;

	private String directory = null;

	private StringBuffer stout = new StringBuffer();

	private String stdoutFileName = null;

	private List<String> inputs = new Vector<String>();

	private List<String> outputs = new Vector<String>();

	private PipedCommand inputCommand = null;

	private PipedCommand outputCommand = null;

	private Process process;

	public PipedCommand(String cmd, String... params) {
		this.cmd = cmd;
		this.params = params;
	}

	public PipedCommand(String cmd) {
		this.cmd = cmd;
	}

	public void setParams(String... params) {
		this.params = params;
	}

	public void setParams(List<String> params) {
		this.params = new String[params.size()];
		for (int i = 0; i < params.size(); i++) {
			this.params[i] = params.get(i);
		}
	}

	public void saveStdOut(String filename) {
		this.stdoutFileName = filename;
		silent = true;
	}

	protected void readFrom(PipedCommand command) {
		inputCommand = command;
		inputCommand.writeTo(this);
	}

	protected void writeTo(PipedCommand command) {
		outputCommand = command;
	}

	protected int execute() {

		if (inputCommand != null) {
			inputCommand.execute();
		}

		List<String> command = new ArrayList<String>();

		command.add(cmd);
		if (params != null) {
			for (String param : params) {
				command.add(param);
			}
		}

		try {

			ProcessBuilder builder = new ProcessBuilder(command);
			// builder.redirectErrorStream(true);
			if (directory != null) {
				builder.directory(new File(directory));
			}

			process = builder.start();

			Thread inputStreamHandler = null;

			if (outputCommand == null) {
				CommandStreamHandler handler = new CommandStreamHandler(
						process.getInputStream(), stdoutFileName);
				handler.setSilent(silent);
				inputStreamHandler = new Thread(handler);
				inputStreamHandler.start();
			}

			if (inputCommand != null) {

				CommandStreamPipeHandler handler = new CommandStreamPipeHandler(
						inputCommand.getOutputStream(),
						process.getOutputStream());
				Thread pipeHandler = new Thread(handler);
				pipeHandler.start();

			}

			Thread errorStreamHandler = new Thread(new CommandStreamHandler(
					process.getErrorStream()));

			errorStreamHandler.start();

			if (outputCommand == null) {
				process.waitFor();

				inputStreamHandler.interrupt();
				errorStreamHandler.interrupt();
				inputStreamHandler.join();
				errorStreamHandler.join();

				if (process.exitValue() != 0) {
					return process.exitValue();
				} else {
					process.destroy();
				}
			}
			if (deleteInput) {
				new File(cmd).delete();
			}

			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public InputStream getOutputStream() {
		return process.getInputStream();
	}

	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	public boolean isDeleteInput() {
		return deleteInput;
	}

	public void setDeleteInput(boolean deleteInput) {
		this.deleteInput = deleteInput;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}

	@Override
	public String toString() {
		String result = cmd;
		if (params != null) {
			for (String param : params) {
				result += " " + param;
			}
		}
		return result;
	}

	public String getStdOut() {
		return stout.toString();
	}

	public void addInput(String input) {
		inputs.add(input);
	}

	public void addOutput(String output) {
		outputs.add(output);
	}

	public List<String> getInputs() {
		return inputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	private boolean isNoFile(String param) {
		return !inputs.contains(param) && !outputs.contains(param);
	}

	public String getSignature() {

		String fullCommand = cmd;
		for (String param : params) {
			if (isNoFile(param)) {
				fullCommand += param;
			}
		}

		return DigestUtils.md5Hex(fullCommand);

	}

	public String getName() {
		return cmd;
	}

}
