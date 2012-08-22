package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;

@Ceylon(major = 3)
@Class(extendsType="ceylon.language.Exception")
public class RecursiveInitializationException extends Exception {
	private static final long serialVersionUID = -1717584352791182643L;
	public RecursiveInitializationException() {
		super(String.instance("Name could not be initialized due to recursive access during initialization"), null);
	}
}
