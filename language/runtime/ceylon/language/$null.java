package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
class $nothing extends Nothing {
    
    private final static Nothing value = new $nothing();
    
    static Nothing getNothing(){
        return value;
    }
}

@Ceylon @Attribute
public class $null {    
    public static Nothing getNull(){
        return $nothing.getNothing();
    }
}
