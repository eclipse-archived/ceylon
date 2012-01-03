package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class smaller {
	
    public static Comparison getSmaller(){
        return Comparison.SMALLER;
    }
}
