package genepi.filter;

import java.util.List;
import java.util.Vector;

public abstract class AbstractFilter<e> {

	public List<e> apply(List<e> list) {
		List<e> result = new Vector<e>();
		for (e object : list) {
			if (accept(object)) {
				result.add(object);
			}
		}
		return result;
	}

	public abstract boolean accept(e object);

}
