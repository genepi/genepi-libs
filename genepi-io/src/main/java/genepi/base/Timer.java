package genepi.base;

public class Timer {

	private long start = 0;

	private long end = 0;

	public void start() {
		start = System.currentTimeMillis();
	}

	public void stop() {
		end = System.currentTimeMillis();
	}

	public void println() {
		System.out.println("Execution Time: " + (end - start) / 1000
				+ " seconds");
	}

	public void println(String task) {
		System.out.println("[" + task + "] Execution Time: " + (end - start)
				/ 1000 + " seconds");
	}

}
