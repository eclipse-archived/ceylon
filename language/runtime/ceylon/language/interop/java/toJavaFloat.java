package ceylon.language.interop.java;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class toJavaFloat {
    public static float toJavaFloat(@TypeInfo("ceylon.language.Float") double c){
        return (float) c;
    }
}
