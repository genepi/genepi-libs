package genepi.hadoop.importer;

import genepi.hadoop.HdfsUtil;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.CountingOutputStream;

public class LocalImporterHttp implements IImporter {

	private String url;

	private String path;

	private CountingOutputStream t;

	private String error;

	public LocalImporterHttp(String url, String path) {

		this.url = url.split(";")[0];
		this.path = path;

	}

	public long getFileSize() {

		try {
			URL webUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) webUrl.openConnection();
			if (conn.getResponseCode() == 404){
				error = "Url '" + url + "' not found";

				return -1;
			}
			int size = conn.getContentLength();
			conn.disconnect();
			return size;

		} catch (MalformedURLException e) {
			error = "The provided url '" + url + "' is mail formed. ";
			return -1;
		} catch (Exception e) {	
			e.printStackTrace();
			error = "The provided url '" + url + "' is not valid. ";
			return -1;

		}

	}

	@Override
	public boolean importFiles() {
		return importFiles(null);
	}

	@Override
	public boolean importFiles(String extension) {

		try {
			return importIntoLocal(url, path);
		} catch (IOException e) {
			error = e.getMessage();
			return false;
		}

	}

	public boolean importIntoLocal(String weburl, String path) throws IOException {

		URL url = new URL(weburl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		BufferedInputStream in = new BufferedInputStream(conn.getInputStream());

		// path in hdfs
		String[] tiles = weburl.split("/");
		String name = tiles[tiles.length - 1];

		String target = HdfsUtil.path(path, name);

		FileOutputStream out = new FileOutputStream(target);

		t = new CountingOutputStream(out);

		IOUtils.copyLarge(in, t);

		in.close();
		out.close();

		return true;
	}

	@Override
	public List<FileItem> getFiles() {
		long size = getFileSize();
		if (size >= 0) {
			List<FileItem> items = new Vector<FileItem>();
			FileItem file = new FileItem();
			file.setText(FilenameUtils.getName(url));
			file.setPath("/");
			file.setId("/");
			file.setSize(FileUtils.byteCountToDisplaySize(size));
			items.add(file);
			return items;
		} else {
			return null;
		}
	}

	@Override
	public String getErrorMessage() {
		return error;
	}

}
