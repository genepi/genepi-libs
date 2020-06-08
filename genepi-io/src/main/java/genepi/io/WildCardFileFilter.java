package genepi.io;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class WildCardFileFilter implements FileFilter {
	private String _pattern;

	public WildCardFileFilter(String pattern) {
		_pattern = pattern.replace("*", ".*").replace("?", ".");
	}

	public boolean accept(File file) {
		return Pattern.compile(_pattern).matcher(file.getName()).find();
	}
}