package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
class truth extends Boolean {
    
    private final static truth value = new truth();

    public boolean booleanValue() {
        return true;
    }
    
    static truth getTruth(){
        return value;
    }
    
    @Override
    public java.lang.String toString() {
        return "true";
    }
}
