package genepi.hadoop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;

public class PreferenceStore {

	private Properties properties = new Properties();

	public PreferenceStore(File file) {
		load(file);
	}

	public PreferenceStore(Configuration configuration) {
		load(configuration);
	}
	
	public void load(File file) {
		try {
			properties.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load(Configuration configuration) {
		Map<String, String> pairs = configuration.getValByRegex("cloudgene.*");
		for (String key : pairs.keySet()) {
			String cleanKey = key.replace("cloudgene.", "");
			String value = pairs.get(key);
			properties.setProperty(cleanKey, value);
		}
	}

	public void write(Configuration configuration) {

		for (Object key : properties.keySet()) {
			String newKey = "cloudgene." + key.toString();
			String value = properties.getProperty(key.toString());
			configuration.set(newKey, value);
		}

	}

	public String getString(String key) {
		return properties.getProperty(key);
	}

	public void setString(String key, String value) {
		properties.setProperty(key, value);
	}

}
