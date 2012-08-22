package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 3) @Object
public class exhausted_ extends Finished {
    
    private final static exhausted_ exhausted = new exhausted_();
    
    public static exhausted_ getExhausted(){
        return exhausted;
    }

    @Override
    public java.lang.String toString() {
        return "exhausted";
    }
}
