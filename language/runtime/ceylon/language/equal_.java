package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 4) @Object
public class equal_ extends Comparison {
	
	private equal_() {
		super("equal");
	}
	
	private static final equal_ equal = new equal_();
	
    public static equal_ getEqual$(){
        return equal;
    }
}
