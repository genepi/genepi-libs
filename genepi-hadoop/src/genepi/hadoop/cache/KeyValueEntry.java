package genepi.hadoop.cache;

public class KeyValueEntry {

	private String key;

	private String value;

	private String signature;

	public KeyValueEntry(String signature) {
		this.signature = signature;
	}

	public KeyValueEntry(String signature, String key, String value) {
		this.signature = signature;
		this.key = key;
		this.value = value;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	@Override
	public boolean equals(Object obj) {
		return signature.equals(((KeyValueEntry) obj).getSignature());
	}

	@Override
	public String toString() {
		return signature + "\t" + key + "\t " + value;
	}

	public static KeyValueEntry parse(String line) {
		String[] tiles = line.split("\t");
		return new KeyValueEntry(tiles[0], tiles[1], tiles[2]);
	}

}
