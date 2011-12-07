package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@Method
public final class min {
    
    private min() {
    }
    
    @TypeParameters(@TypeParameter(value="Value", satisfies="ceylon.language.Comparable<Value>"))
    @TypeInfo("Value")
    public static <Value extends Comparable<? super Value>>Value min(@Name("values")
    @TypeInfo("ceylon.language.Sequence<Value>")
    final Sequence<? extends Value> values) {
        Value min = values.getFirst();
        for (Iterator<? extends Value> $val$iter$0 = values.getRest().getIterator(); $val$iter$0 != null; $val$iter$0 = $val$iter$0.getTail()) {
            final Value val = $val$iter$0.getHead();
            if (val.compare(min).smaller()) {
                min = val;
            }
        }
        return min;
    }
}
