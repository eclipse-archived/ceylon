package ceylon.language.interop.java;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class toJavaInt {
    public static int toJavaInt(@TypeInfo("ceylon.language.Integer") long c){
        return (int) c;
    }
}
