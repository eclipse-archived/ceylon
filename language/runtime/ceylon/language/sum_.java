package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public final class sum_ {
    
    private sum_() {
    }
    
    @TypeParameters(@TypeParameter(value="Value", satisfies="ceylon.language::Summable<Value>"))
    @TypeInfo("Value")
    public static <Value extends Summable<Value>>Value sum(@Name("values")
    @TypeInfo("ceylon.language::Sequence<Value>")
    final Sequence<? extends Value> values) {
        Value sum = values.getFirst();
        java.lang.Object $tmp;
        for (Iterator<? extends Value> $val$iter$0 = values.getRest().getIterator(); 
                !(($tmp = $val$iter$0.next()) instanceof Finished);) {
            sum = sum.plus((Value) $tmp);
        }
        return sum;
    }
}
