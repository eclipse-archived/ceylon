package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class exhausted {
    private final static Finished value = new Finished(){};
    
    public static Finished getExhausted(){
        return value;
    }
}
