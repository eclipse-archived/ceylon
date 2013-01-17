package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 4) @Object
public class false_ extends Boolean {
    
    private final static false_ value = new false_();

    public static false_ getFalse$(){
        return value;
    }

    @Override
    public boolean booleanValue() {
        return false;
    }
    
    @Override
    public java.lang.String toString() {
        return "false";
    }
}
