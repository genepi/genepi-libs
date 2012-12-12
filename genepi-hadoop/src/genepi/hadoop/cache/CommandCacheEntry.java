package genepi.hadoop.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import genepi.hadoop.command.Command;
import genepi.hadoop.command.ICommand;

public class CommandCacheEntry {

	private String signature;
	private String[] inputFiles;
	private String[] outputFiles;
	private ICommand command;

	private CommandCacheEntry() {

	}

	public CommandCacheEntry(ICommand command) {
		this.command = command;
		signature = command.getSignature();
		inputFiles = new String[command.getInputs().size()];
		for (int i = 0; i < command.getInputs().size(); i++) {
			inputFiles[i] = getMd5Heash(command.getInputs().get(i));
		}
		outputFiles = new String[command.getOutputs().size()];
	}

	public boolean isEqual(CommandCacheEntry entry) {
		String mySignature = entry.getSignature();
		if (signature.equals(mySignature)) {
			for (int i = 0; i < entry.getInputFiles().length; i++) {
				if (!getInputFiles()[i].equals(entry.getInputFiles()[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	public String getSignature() {
		return signature;
	}

	public String[] getOutputFiles() {
		return outputFiles;
	}

	public String[] getInputFiles() {
		return inputFiles;
	}

	public ICommand getCommand() {
		return command;
	}

	public static String getMd5Heash(String filename) {
		try {
			FileInputStream fis = new FileInputStream(new File(filename));
			String md5 = org.apache.commons.codec.digest.DigestUtils
					.md5Hex(fis);
			return md5;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static CommandCacheEntry parse(String line) {
		String[] tiles = line.split("\t");
		CommandCacheEntry entry = new CommandCacheEntry();
		entry.signature = tiles[0];
		entry.inputFiles = tiles[1].split(" ");
		entry.outputFiles = tiles[2].split(" ");
		return entry;

	}

	@Override
	public String toString() {
		String result = signature;
		result += "\t";
		for (int i = 0; i < inputFiles.length; i++) {
			if (i > 0) {
				result += " ";
			}
			result += inputFiles[i];
		}

		result += "\t";

		for (int i = 0; i < outputFiles.length; i++) {
			if (i > 0) {
				result += " ";
			}
			result += outputFiles[i];
		}
		return result;
	}
	
	public String getName(){
		return command.getName();
	}

}
