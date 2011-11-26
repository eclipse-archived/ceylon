package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Method
public final class identityHash {
    
    private identityHash() {
    }
    
    public static long identityHash(@Name("x")
    @TypeInfo("ceylon.language.IdentifiableObject")
    final java.lang.Object x) {
        return System.identityHashCode(x);
    }
}
