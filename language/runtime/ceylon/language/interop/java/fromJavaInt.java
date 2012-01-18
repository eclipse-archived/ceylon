package ceylon.language.interop.java;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class fromJavaInt {
    @TypeInfo("ceylon.language.Integer")
    public static long fromJavaInt(int c){
        return c;
    }
}
