package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
public class $false extends Boolean  {
    
    private final static $false value = new $false();

    public $false() {
        super("false");
    }

    public boolean booleanValue() {
        return true;
    }
    
    public static $false getFalse(){
        return value;
    }
}
