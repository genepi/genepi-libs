package genepi.hadoop.importer;

import java.util.List;
import java.util.Vector;

public class HdfsImporterS3 implements IImporter {

	private String error = "Not yet implemented.";

	public HdfsImporterS3(String url, String path) {

	}

	@Override
	public boolean importFiles() {
		return false;
	}

	@Override
	public boolean importFiles(String extension) {

		return false;
	}

	@Override
	public List<FileItem> getFiles() {

		List<FileItem> results = new Vector<FileItem>();

				return results;

	}

	@Override
	public String getErrorMessage() {
		return error;
	}


}
