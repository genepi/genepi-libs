package genepi.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Toolbox {

	private Map<String, Class> tools = new HashMap<String, Class>();

	private String command;

	private String[] args;

	public Toolbox(String command, String[] args) {
		this.args = args;
		this.command = command;
	}

	public void addTool(String command, Class clazz) {
		tools.put(command, clazz);
	}

	public void start() throws InstantiationException, IllegalAccessException,
			SecurityException, NoSuchMethodException, IllegalArgumentException,
			InvocationTargetException {

		if (args.length > 0) {

			String tool = args[0];

			String[] newargs = new String[args.length - 1];
			for (int i = 0; i < newargs.length; i++) {
				newargs[i] = args[i + 1];
			}

			Class clazz = tools.get(tool);
			if (clazz != null) {
				Constructor<Tool> constructor = clazz
						.getConstructor(String[].class);
				Tool instance = constructor
						.newInstance(new Object[] { newargs });
				int status = instance.start();

				System.exit(status);

			} else {

				System.out.println("\nUnknown tool: " + tool);

				help();
				System.exit(1);

			}

		} else {

			help();
			System.exit(1);

		}

	}

	private void help() {
		System.out.println("\nusage: " + command + " <tool> <params>\n");

		System.out.println("Available Tools:");

		for (String t : tools.keySet()) {
			System.out.println("  " + t);
		}
		System.out.println("");
	}

}
