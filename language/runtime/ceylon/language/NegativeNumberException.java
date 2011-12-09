package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;

@Ceylon
@Class(extendsType="ceylon.language.Exception")
public class NegativeNumberException extends Exception {
	private static final long serialVersionUID = -906596411923074350L;
	public NegativeNumberException() {
		super(String.instance("negative number"), null);
	}
}
