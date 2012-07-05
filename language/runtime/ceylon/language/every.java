package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@Method
public final class every {
    
    private every() {
    }
    
    @TypeInfo("ceylon.language.Boolean")
    public static boolean every(@Name("values") @Sequenced
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Boolean>")
    final Iterable<? extends Boolean> values) {
        java.lang.Object $tmp;
        for (Iterator<? extends Boolean> $val$iter$0 = values.getIterator(); 
                !(($tmp = $val$iter$0.next()) instanceof Finished);) {
           if (!((Boolean)$tmp).booleanValue()) return false;
        }
        return true;
    }
    @Ignore
    public static boolean every() {
        return true;
    }
}
