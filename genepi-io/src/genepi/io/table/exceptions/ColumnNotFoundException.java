package genepi.io.table.exceptions;

public class ColumnNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ColumnNotFoundException(String message) {
		super(message);
	}
}
