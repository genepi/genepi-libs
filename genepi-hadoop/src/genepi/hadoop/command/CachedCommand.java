package genepi.hadoop.command;

import genepi.hadoop.cache.CommandCache;

public class CachedCommand extends Command {

	public CachedCommand(String cmd, String... params) {
		super(cmd, params);
	}

	public CachedCommand(String cmd) {
		super(cmd);
	}

	@Override
	public int execute() {

		CommandCache cache = CommandCache.getInstance();

		if (cache.isCached(this)) {

			cache.checkOut(this);

			return 0;

		} else {

			int result = super.execute();
			if (result == 0) {
				cache.cache(this);
			}

			return result;

		}

	}

	public boolean isCached() {
		return CommandCache.getInstance().isCached(this);
	}

}
