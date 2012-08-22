package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Sequenced;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class count_ {
    
    private count_() {
    }
    
    @TypeInfo("ceylon.language.Integer")
    public static long count(@Name("values") @Sequenced
    @TypeInfo("ceylon.language.Iterable<ceylon.language.Boolean>")
    final Iterable<? extends Boolean> values) {
        long count=0;
        java.lang.Object $tmp;
        for (Iterator<? extends Boolean> $val$iter$0 = values.getIterator(); 
                !(($tmp = $val$iter$0.next()) instanceof Finished);) {
           if (((Boolean)$tmp).booleanValue()) count++;
        }
        return count;
    }
    @Ignore
    public static long count() {
        return 0;
    }
}
