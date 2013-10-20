package genepi.io.linkage;

public class Marker {

	private String id;

	public Marker(String line) {
		String[] tiles = line.split("\\s{1}(?!\\s)");
		id = tiles[1];
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		Marker marker = (Marker) obj;
		return id.equals(marker.id);
	}

}
