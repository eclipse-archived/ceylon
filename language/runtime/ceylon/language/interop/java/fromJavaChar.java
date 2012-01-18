package ceylon.language.interop.java;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class fromJavaChar {
    @TypeInfo("ceylon.language.Character")
    public static int fromJavaChar(char c){
        return c;
    }
}
