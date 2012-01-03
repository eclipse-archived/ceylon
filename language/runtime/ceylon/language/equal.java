package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class equal {
	
    public static Comparison getEqual(){
        return Comparison.EQUAL;
    }
}
