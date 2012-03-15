package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
public class $false extends Boolean {
    
    private final static $false value = new $false();

    public static $false getFalse(){
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
