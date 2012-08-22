package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 3) @Object
public final class null_ extends Nothing {
    
    private final static null_ value = new null_();
    
    static null_ getNull(){
        return value;
    }
}
