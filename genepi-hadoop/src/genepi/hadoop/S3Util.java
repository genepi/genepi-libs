package genepi.hadoop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class S3Util {

	public static boolean isValidS3Url(String url) {
		if (url.startsWith("s3://")) {
			String temp = url.replaceAll("s3://", "");
			String[] tiles = temp.split("/", 2);
			if (tiles.length == 2) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	public static String getBucket(String url) {
		if (url.startsWith("s3://")) {
			String temp = url.replaceAll("s3://", "");
			String[] tiles = temp.split("/", 2);
			if (tiles.length == 2) {
				return tiles[0];
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String getKey(String url) {
		if (url.startsWith("s3://")) {
			String temp = url.replaceAll("s3://", "");
			String[] tiles = temp.split("/", 2);
			if (tiles.length == 2) {
				return tiles[1];
			} else {
				return null;
			}
		} else {
			return null;
		}

	}

	public static void copyToFile(String url, File file) throws IOException {
		if (isValidS3Url(url)) {
			String bucket = getBucket(url);
			String key = getKey(url);
			copyToFile(bucket, key, file);
		}
	}

	public static void copyToFile(String bucket, String key, File file) throws IOException {

		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

		S3Object o = s3.getObject(bucket, key);
		S3ObjectInputStream s3is = o.getObjectContent();
		FileOutputStream fos = new FileOutputStream(file);
		byte[] read_buf = new byte[1024];
		int read_len = 0;
		while ((read_len = s3is.read(read_buf)) > 0) {
			fos.write(read_buf, 0, read_len);
		}
		s3is.close();
		fos.close();

	}

	public static ObjectListing listObjects(String url) throws IOException {

		final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

		if (isValidS3Url(url)) {
			
			String bucket = getBucket(url);
			String key = getKey(url);

			ObjectListing objects = s3.listObjects(bucket, key);
			
			return objects;
		}

		return null;
	}

}
