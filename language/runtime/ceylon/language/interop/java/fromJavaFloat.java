package ceylon.language.interop.java;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class fromJavaFloat {
    @TypeInfo("ceylon.language.Float")
    public static double fromJavaFloat(float c){
        return c;
    }
}
