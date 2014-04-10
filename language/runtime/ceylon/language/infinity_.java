package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon(major = 7) @Attribute
public class infinity_ {
    
    public static double get_(){
        return Double.POSITIVE_INFINITY;
    }
}
