package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon(major = 2) @Object
public class $true extends Boolean {
    
    private final static $true value = new $true();

    public static $true getTrue(){
        return value;
    }

    @Override
    public boolean booleanValue() {
        return true;
    }
    
    @Override
    public java.lang.String toString() {
        return "true";
    }
}
