package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 4) @Object
public class smaller_ extends Comparison {
	
	private smaller_() {
		super("smaller");
	}
	
	private static final smaller_ smaller = new smaller_();
	
    public static smaller_ getSmaller$(){
    	return smaller;
    }
}
