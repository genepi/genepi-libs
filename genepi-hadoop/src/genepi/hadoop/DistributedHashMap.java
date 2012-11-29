package genepi.hadoop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;

public class DistributedHashMap {

	public static String BARCODE = "cloudgene.maps";

	private Configuration configuration;

	private String id;

	private String myBarcode = null;

	private Map<String, String> map = new HashMap<String, String>();

	public DistributedHashMap(String id, Configuration configuration) {
		this.id = id;
		this.configuration = configuration;
		myBarcode = BARCODE + "." + id + ".";
	}

	public static DistributedHashMap load(String id, Configuration configuration) {
		DistributedHashMap result = new DistributedHashMap(id, configuration);
		Map<String, String> pairs = configuration
				.getValByRegex(result.myBarcode + "*");
		for (String key : pairs.keySet()) {
			String cleanKey = key.replace(result.myBarcode, "");
			String value = pairs.get(key);
			result.map.put(cleanKey, value);
		}
		return result;

	}

	public String getId() {
		return id;
	}

	public void put(String key, String value) {
		map.put(key, value);
		configuration.set(myBarcode + key, value);
	}

	public String get(String key) {
		return map.get(key);
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	public Collection<String> values() {
		return map.values();
	}

}
