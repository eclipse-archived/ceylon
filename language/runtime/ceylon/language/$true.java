package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
public class $true extends Boolean {
    
    private final static $true value = new $true();

    public $true() {
        super("true");
    }

    public boolean booleanValue() {
        return true;
    }
    
    public static $true getTrue(){
        return value;
    }
}
