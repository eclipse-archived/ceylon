package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 7)
@Method
public final class identityHash_ {
    
    private identityHash_() {
    }
    
    public static long identityHash(@Name("identifiable")
    @TypeInfo("ceylon.language::Identifiable")
    final java.lang.Object identifiable) {
        return System.identityHashCode(identifiable);
    }
}
