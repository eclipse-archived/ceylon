package ceylon.language;

public class OverflowException extends Exception {
	private static final long serialVersionUID = -2715586352972281655L;
	public OverflowException() {
		super("numeric overflow", null);
	}
}
