package genepi.base;


public class TestTool extends Tool {

	public TestTool(String[] args) {
		super(args);
	}

	@Override
	public void init() {

		System.out.println("Ped-Diff\n\n");

	}

	@Override
	public void createParameters() {
		addParameter("ped", "input ped filename");
		addParameter("number", "input ped filename", Tool.DOUBLE);
	}

	@Override
	public int run() {

		System.out.println(getValue("number"));
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] temp = new String[] { "ped", "luk", "--number", "5.0" };

		(new TestTool(temp)).start();

	}

}
