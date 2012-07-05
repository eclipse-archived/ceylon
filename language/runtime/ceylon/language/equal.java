package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 2) @Object
public class equal extends Comparison {
	
	private equal() {
		super("equal");
	}
	
	private static final equal equal = new equal();
	
    public static equal getEqual(){
        return equal;
    }
}
