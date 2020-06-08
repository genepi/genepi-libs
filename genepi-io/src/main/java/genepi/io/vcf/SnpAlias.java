package genepi.io.vcf;

public class SnpAlias {

	private String oldName;

	private String newName;

	public SnpAlias(String line) {
		String[] tiles = line.split(" ");
		if (tiles.length == 1) {
			tiles = line.split("\t");
			oldName = tiles[1];
			newName = tiles[0].replaceAll("chr", "");;
		} else {
			oldName = tiles[0];
			newName = tiles[1];
		}
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

}
