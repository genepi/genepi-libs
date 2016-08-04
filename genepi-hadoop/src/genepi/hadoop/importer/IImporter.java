package genepi.hadoop.importer;

import java.util.List;

public interface IImporter {

	public boolean importFiles();
	
	public boolean importFiles(String extension);

	public String getErrorMessage();

	public List<FileItem> getFiles();

}
