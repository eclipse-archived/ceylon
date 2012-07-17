package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@Method
public final class identical_ {
    
    private identical_() {
    }
    
    public static boolean identical(@Name("x")
    @TypeInfo("ceylon.language.Identifiable")
    final java.lang.Object x, 
    @Name("y")
    @TypeInfo("ceylon.language.Identifiable")
    final java.lang.Object y) {
        return x==y;
    }
}
