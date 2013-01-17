package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 4) @Object
public class larger_ extends Comparison {
	
	private larger_() {
		super("larger");
	}
	
	private static final larger_ larger = new larger_();
	
    public static larger_ getLarger$(){
        return larger;
    }
}
