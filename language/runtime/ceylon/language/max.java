package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@Method
public final class max {
    
    private max() {
    }
    
    @TypeParameters(@TypeParameter(value="Value", satisfies="ceylon.language.Comparable<Value>"))
    @TypeInfo("Value")
    public static <Value extends Comparable<? super Value>>Value max(@Name("values")
    @TypeInfo("ceylon.language.Sequence<Value>")
    final Sequence<? extends Value> values) {
        Value max = values.getFirst();
        java.lang.Object $tmp;
        for (Iterator<? extends Value> $val$iter$0 = values.getRest().getIterator(); 
                !(($tmp = $val$iter$0.next()) instanceof Finished);) {
            final Value val = (Value) $tmp;
            if (val.compare(max).largerThan()) {
                max = val;
            }
        }
        return max;
    }
}
