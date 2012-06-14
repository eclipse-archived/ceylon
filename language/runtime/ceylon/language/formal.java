package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;

@Ceylon(major = 1)
@Method
public final class formal {
    public static Nothing formal(){
        return null;
    }
    private formal(){}
}
