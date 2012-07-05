package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@Method
public final class any {
    
    private any() {
    }
    
    @TypeInfo("ceylon.language.Boolean")
    public static boolean any(@Name("values") @Sequenced
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Boolean>")
    final Iterable<? extends Boolean> values) {
        java.lang.Object $tmp;
        for (Iterator<? extends Boolean> $val$iter$0 = values.getIterator(); 
                !(($tmp = $val$iter$0.next()) instanceof Finished);) {
           if (((Boolean)$tmp).booleanValue()) return true;
        }
        return false;
    }
    @Ignore
    public static boolean any() {
        return false;
    }
}
