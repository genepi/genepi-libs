package genepi.hadoop.common;

import genepi.hadoop.HdfsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandLineContext extends WorkflowContext {

	private Map<String, String> params;

	private String jobId;

	private Map<String, String> config;

	public CommandLineContext(String[] args) throws Exception {

		params = new HashMap<String, String>();

		for (int i = 0; i < args.length; i++) {
			if (args[i].startsWith("--")) {
				String key = args[i];
				i++;
				if (i >= args.length) {
					throw new Exception("");
				}
				String value = args[i];
				params.put(key.replace("--", ""), value);
			}
		}

		jobId = "job_" + System.currentTimeMillis();

	}

	@Override
	public String getInput(String param) {
		return params.get(param);
	}

	@Override
	public String getJobId() {
		return jobId;
	}

	@Override
	public String getOutput(String param) {
		return params.get(param);
	}

	@Override
	public String get(String param) {
		return params.get(param);
	}

	@Override
	public void println(String line) {
		System.out.println(line);
	}

	@Override
	public void log(String line) {
		System.out.println(line);
	}

	@Override
	public String getWorkingDirectory() {
		return ".";
	}

	@Override
	public boolean sendMail(String subject, String body) throws Exception {
		return false;
	}

	@Override
	public boolean sendMail(String to, String subject, String body) throws Exception {
		return false;
	}
	
	@Override
	public boolean sendNotification(String body) throws Exception {
		return false;
	}

	@Override
	public Set<String> getInputs() {
		return params.keySet();
	}

	@Override
	public void setInput(String input, String value) {
		params.put(input, value);
	}

	@Override
	public void setOutput(String input, String value) {
		params.put(input, value);
	}

	@Override
	public void incCounter(String name, int value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void submitCounter(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Integer> getCounters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getData(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createLinkToFile(String id) {
		// TODO Auto-generated method stub
		return "[NOT AVAILABLE]";
	}

	@Override
	public String createLinkToFile(String id, String filename) {
		// TODO Auto-generated method stub
		return "[NOT AVAILABLE]";
	}

	@Override
	public String getJobName() {
		return jobId;
	}

	@Override
	public String getHdfsTemp() {
		return HdfsUtil.path("temp", jobId);
	}

	@Override
	public String getLocalTemp() {
		return "temp/" + jobId;
	}

	@Override
	public void message(String message, int type) {

		switch (type) {
		case OK:
			System.out.println("[OK] " + message);
			break;
		case ERROR:
			System.out.println("[ERROR] " + message);
			break;
		case WARNING:
			System.out.println("[WARN] " + message);
			break;
		case RUNNING:
			System.out.println("[RUNNING] " + message);
			break;
		default:
			System.out.println("[INFO] " + message);
		}

	}

	@Override
	public void beginTask(String name) {
		message(name, RUNNING);
	}

	@Override
	public void updateTask(String name, int type) {
		message(name, type);
	}

	@Override
	public void endTask(String message, int type) {
		message(message, type);
	}

	@Override
	public void setConfig(Map<String, String> config) {
		this.config = config;
	}

	@Override
	public String getConfig(String param) {
		if (config != null) {
			return config.get(param);
		} else {
			return null;
		}
	}

}
