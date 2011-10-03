package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;

@Ceylon
@Method
public final class shared {
    public static Nothing shared(){
        return null;
    }
    private shared(){}
}
