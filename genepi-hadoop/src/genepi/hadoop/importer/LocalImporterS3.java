package genepi.hadoop.importer;

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import genepi.hadoop.HdfsUtil;
import genepi.hadoop.S3Util;

public class LocalImporterS3 implements IImporter {

	private String url;

	private String path;

	private String error;

	public LocalImporterS3(String url, String path) {

		this.url = url.replaceAll(";", "");
		this.path = path;
	}

	@Override
	public boolean importFiles() {
		return importFiles(null);
	}

	@Override
	public boolean importFiles(String extension) {

		try {

			ObjectListing listing = S3Util.listObjects(url);

			for (S3ObjectSummary summary : listing.getObjectSummaries()) {
				String bucket = summary.getBucketName();
				String key = summary.getKey();

				System.out.println("Found file" + bucket + "/" + key);

				
				if (matchesExtension(key, extension)) {
					String[] temp = key.split("/");
					String name = temp[temp.length - 1];
					String target = HdfsUtil.path(path, name);
					File file = new File(target);
					System.out.println("Copy file from " + bucket + "/" + key + " to " + target);
					S3Util.copyToFile(bucket, key, file);
				}
			}

			return true;

		} catch (Exception e) {
			error = e.getMessage();
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public List<FileItem> getFiles() {

		List<FileItem> results = new Vector<FileItem>();

		try {

			ObjectListing listing = S3Util.listObjects(url);

			for (S3ObjectSummary summary : listing.getObjectSummaries()) {
				String key = summary.getKey();

				String[] temp = key.split("/");
				String name = temp[temp.length - 1];

				FileItem item = new FileItem();
				item.setText(name);
				item.setPath("/");
				item.setId("/");
				item.setSize(FileUtils.byteCountToDisplaySize(summary.getSize()));

				results.add(item);

			}

			return results;

		} catch (Exception e) {
			error = e.getMessage();
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public String getErrorMessage() {
		return error;
	}

	private boolean matchesExtension(String key, String extensions) {
		boolean needImport = false;
		if (extensions == null) {
			return true;
		}

		if (!needImport) {
			String[] exts = extensions.split("|");
			for (String ext : exts) {
				if (!needImport) {
					if (key.endsWith(ext)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
