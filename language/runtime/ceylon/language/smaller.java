package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class smaller {
	
    public static Comparison getSmaller(){
        return Comparison.SMALLER;
    }
}
