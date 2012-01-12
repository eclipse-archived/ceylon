package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class $finished {
    private final static Finished value = new Finished(){};
    
    public static Finished getFinished(){
        return value;
    }
}
