package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 1)
@Method
public final class min {
    
    private min() {
    }
    
    @TypeParameters({@TypeParameter(value="Value", 
            satisfies={"ceylon.language.Comparable<Value>", "Result"}),
                     @TypeParameter(value="Result")})
    @TypeInfo("Result")
    public static <Value extends Comparable<? super Value>, Result> 
    Value min(@Name("values")
    @TypeInfo("ceylon.language.Iterable<Value>&ceylon.language.ContainerWithFirstElement<Result>")
    final Iterable<?> values) {
        Value min = (Value) values.getFirst();
        if (min!=null) {
        	java.lang.Object $tmp;
        	for (Iterator<? extends Value> $val$iter$0 = (Iterator<? extends Value>)values.getRest().getIterator(); 
        			!(($tmp = $val$iter$0.next()) instanceof Finished);) {
        		final Value val = (Value) $tmp;
        		if (val.compare(min).smallerThan()) {
        			min = val;
        		}
        	}
        }
        return min;
    }
}
