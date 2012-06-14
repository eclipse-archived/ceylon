package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 1) @Object
public class smaller extends Comparison {
	
	private smaller() {
		super("smaller");
	}
	
	private static final smaller smaller = new smaller();
	
    public static smaller getSmaller(){
    	return smaller;
    }
}
