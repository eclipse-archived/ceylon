package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;

@Ceylon
@Method
public final class shared {
    public static Nothing shared(){
        return null;
    }
    private shared(){}
}
