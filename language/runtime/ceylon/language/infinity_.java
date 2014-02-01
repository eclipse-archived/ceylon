package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon(major = 6) @Attribute
public class infinity_ {
    
	@ceylon.language.SharedAnnotation$annotation$
    public static double get_(){
        return Double.POSITIVE_INFINITY;
    }
}
