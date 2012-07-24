package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon(major = 2) @Attribute
public class infinity {
    
    public static double getInfinity(){
        return Double.POSITIVE_INFINITY;
    }
}
