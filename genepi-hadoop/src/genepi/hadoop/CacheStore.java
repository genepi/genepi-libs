package genepi.hadoop;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;

public class CacheStore {

	private Configuration conf;

	public CacheStore(Configuration conf) {
		this.conf = conf;
	}

	public String getLocalPath(String name) throws IOException {

		Path[] files = DistributedCache.getLocalCacheFiles(conf);
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().equals(name)) {
				return files[i].toUri().getPath();
			}
		}
		return null;
	}

	public void addFile(String filename) {
		URI uri = new Path(filename).toUri();
		DistributedCache.addCacheFile(uri, conf);
	}

}
