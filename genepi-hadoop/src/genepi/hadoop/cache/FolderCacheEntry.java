package genepi.hadoop.cache;

public class FolderCacheEntry {

	private String folder;

	private String signature;

	public FolderCacheEntry(String signature) {
		this.signature = signature;
	}

	public FolderCacheEntry(String signature, String folder) {
		this.signature = signature;
		this.folder = folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFolder() {
		return folder;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	@Override
	public boolean equals(Object obj) {
		return signature.equals(((FolderCacheEntry) obj).getSignature());
	}

	@Override
	public String toString() {
		return signature + "\t" + folder;
	}

	public static FolderCacheEntry parse(String line) {
		String[] tiles = line.split("\t");
		return new FolderCacheEntry(tiles[0], tiles[1]);
	}

}
