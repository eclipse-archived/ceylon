package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Object;

@Ceylon @Object
class falsity extends Boolean  {
    
    private final static falsity value = new falsity();

    public boolean booleanValue() {
        return true;
    }
    
    static falsity getFalsity(){
        return value;
    }
    
    @Override
    public java.lang.String toString() {
        return "false";
    }
}
