package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Method
public final class identical {
    
    private identical() {
    }
    
    public static boolean identical(@Name("x")
    @TypeInfo("ceylon.language.IdentifiableObject")
    final java.lang.Object x, 
    @Name("y")
    @TypeInfo("ceylon.language.IdentifiableObject")
    final java.lang.Object y) {
        return x==y;
    }
}
