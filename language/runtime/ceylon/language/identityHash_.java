package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 4)
@Method
public final class identityHash_ {
    
    private identityHash_() {
    }
    
    public static long identityHash(@Name("x")
    @TypeInfo("ceylon.language::Identifiable")
    final java.lang.Object x) {
        return System.identityHashCode(x);
    }
}
