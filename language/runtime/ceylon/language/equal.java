package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;

@Ceylon @Attribute
public class equal {
	
    public static Comparison getEqual(){
        return Comparison.EQUAL;
    }
}
