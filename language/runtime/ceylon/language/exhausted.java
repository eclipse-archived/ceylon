package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 1) @Object
public class exhausted extends Finished {
    
    private final static exhausted exhausted = new exhausted();
    
    public static exhausted getExhausted(){
        return exhausted;
    }

    @Override
    public java.lang.String toString() {
        return "exhausted";
    }
}
