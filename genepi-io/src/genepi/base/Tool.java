package genepi.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public abstract class Tool {

	private CommandLineParser parser;

	private Options options;

	private CommandLine line = null;

	public static final int FLAG = 0;

	public static final int INTEGER = 1;

	public static final int STRING = 2;

	public static final int DOUBLE = 4;

	private Map<String, Integer> parameters;

	private String[] args;

	public Tool(String[] args) {

		this.args = args;
		parser = new PosixParser();
		options = new Options();
		parameters = new HashMap<String, Integer>();

	}

	public void addParameter(String param, String description) {
		addParameter(param, description, STRING);
	}

	public void addParameter(String param, String description, int type) {

		Option option = new Option(null, param, true, description);
		option.setRequired(true);

		switch (type) {
		case INTEGER:
			option.setArgName("int");
			break;

		case DOUBLE:
			option.setArgName("double");
			break;

		case STRING:
			option.setArgName("string");
			break;

		default:
			throw new IllegalArgumentException();

		}

		options.addOption(option);
		parameters.put(param, type);

	}

	public void addFlag(String param, String description) {

		Option option = new Option(null, param, false, description);
		option.setRequired(false);
		options.addOption(option);
		parameters.put(param, FLAG);
	}

	public void addOptionalParameter(String param, String description, int type) {

		Option option = new Option(null, param, true, description);
		option.setRequired(false);

		switch (type) {
		case INTEGER:
			option.setType(Integer.class);
			break;

		case DOUBLE:
			option.setType(Double.class);
			break;

		case STRING:
			option.setType(String.class);
			break;

		default:
			throw new IllegalArgumentException();

		}

		options.addOption(option);
		parameters.put(param, type);

	}

	public Object getValue(String param) {

		String value = line.getOptionValue(param);
		int type = parameters.get(param);

		switch (type) {
		case INTEGER:
			return Integer.parseInt(value);

		case DOUBLE:
			return Double.parseDouble(value);

		case STRING:
			return value;

		default:
			throw new IllegalArgumentException();
		}

	}
	
	public String[] getRemainingArgs() {
		return line.getArgs();
	}

	public boolean isFlagSet(String param) {

		return line.hasOption(param);

	}

	public void help() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(" ", options, true);
	}

	public abstract void init();

	public abstract int run();

	public abstract void createParameters();

	/**
	 * @param args
	 */
	public int start() {

		createParameters();

		boolean error = false;

		try {

			init();

			line = parser.parse(options, args);

		} catch (Exception e) {

			System.out.println("Error: " + e.getMessage() + "\n");
			help();
			error = true;

		}

		if (!error) {

			// check datatypes
			try {
				for (String param : parameters.keySet()) {

					if (parameters.get(param) != FLAG) {

						getValue(param);

					}
				}

			} catch (Exception e) {
				System.out.println("Datatype Error: " + e.getMessage() + "\n");
				help();
				error = true;
			}

			if (!error) {

				int res = run();
				return res;
			}

		}

		return -1;
	}
}
