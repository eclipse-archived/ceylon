package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class positiveInfinity {
    
    public static double getPositiveInfinity(){
        return Double.POSITIVE_INFINITY;
    }
}
