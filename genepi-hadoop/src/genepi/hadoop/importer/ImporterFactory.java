package genepi.hadoop.importer;

import java.util.List;
import java.util.Vector;

public class ImporterFactory {

	public static boolean needsImport(String url) {
		return url.startsWith("sftp://") || url.startsWith("http://") || url.startsWith("https://")
				|| url.startsWith("ftp://") || url.startsWith("s3://");
	}

	public static IImporter createImporter(String url, String target) {

		if (url.startsWith("sftp://")) {
			if (target != null && target.startsWith("hdfs://")) {
				return new HdfsImporterSftp(url, target);
			} else {
				return new LocalImporterSftp(url, target);
			}
		}
		
		if (url.startsWith("s3://")) {
			if (target != null && target.startsWith("hdfs://")) {
				return new HdfsImporterS3(url, target);
			} else {
				return new LocalImporterS3(url, target);
			}
		}

		if (url.startsWith("http://") || url.startsWith("https://")) {
			if (target != null && target.startsWith("hdfs://")) {
				return new HdfsImporterHttp(url, target);
			} else {
				return new LocalImporterHttp(url, target);
			}
		}

		if (url.startsWith("ftp://")) {
			if (target.startsWith("hdfs://")) {
				return new HdfsImporterFtp(url, target);
			} else {
				return new LocalImporterFtp(url, target);
			}
		}

		return null;
	}

	public static List<String> parseImportString(String input) {

		List<String> results = new Vector<String>();

		String[] urlList = input.split(";")[0].split("\\s+");

		String username = "";
		if (input.split(";").length > 1) {
			username = input.split(";")[1];
		}

		String password = "";
		if (input.split(";").length > 2) {
			password = input.split(";")[2];
		}

		for (String url2 : urlList) {

			String url = url2 + ";" + username + ";" + password;
			results.add(url);
		}

		return results;

	}

}
