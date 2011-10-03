package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;

@Ceylon
@Method
public final class actual {
    public static Nothing actual(){
        return null;
    }
    private actual(){}
}
