package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class negativeInfinity {
    
    public static double getNegativeInfinity(){
        return Double.NEGATIVE_INFINITY;
    }
}
