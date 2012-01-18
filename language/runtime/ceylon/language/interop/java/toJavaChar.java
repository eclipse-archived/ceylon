package ceylon.language.interop.java;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class toJavaChar {
    public static char toJavaChar(@TypeInfo("ceylon.language.Character") int c){
        return (char) c;
    }
}
