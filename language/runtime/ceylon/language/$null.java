package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
class $nothing {
    private final static Nothing value = new Nothing(){};
    
    static Nothing getNothing(){
        return value;
    }
}

@Ceylon @Attribute
public class $null {
    private final static Nothing value = $nothing.getNothing();
    
    public static Nothing getNull(){
        return value;
    }
}
