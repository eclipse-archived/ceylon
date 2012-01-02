package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;

@Ceylon @Attribute
public class $true {
    private final static Boolean value = new Boolean("true"){
        @Override
        public boolean booleanValue() {
            return true;
        }
    };

public static Boolean getTrue(){
    return value;
}
}
