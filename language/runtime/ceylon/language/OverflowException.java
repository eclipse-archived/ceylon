package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

@Ceylon(major = 2)
@Class(extendsType="ceylon.language.Exception")
public class OverflowException extends Exception {
	private static final long serialVersionUID = -2715586352972281655L;
	public OverflowException() {
		super(String.instance("numeric overflow"), null);
	}
}
