package ceylon.language;

public class NegativeNumberException extends Exception {
	private static final long serialVersionUID = -906596411923074350L;
	public NegativeNumberException() {
		super(String.instance("negative number"), null);
	}
}
