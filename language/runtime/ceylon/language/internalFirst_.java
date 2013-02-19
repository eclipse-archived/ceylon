package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 4)
@Method
final class internalFirst_ {
    
    private internalFirst_() {}
    
    @TypeInfo("Absent|Value")
    @TypeParameters({@TypeParameter(value = "Value", variance = Variance.OUT),
                     @TypeParameter(value = "Absent", variance = Variance.OUT, 
                             satisfies = "ceylon.language::Null")})
    static <Value,Absent> java.lang.Object internalFirst(
            @Ignore TypeDescriptor $reifiedValue,
            @Ignore TypeDescriptor $reifiedAbsent,
            @Name("values")
    @TypeInfo("ceylon.language::Iterable<Value,Absent>")
    final Iterable<? extends Value, ?> values) {
        java.lang.Object first = values.getIterator().next();
        if (first instanceof Finished) {
        	//TODO: assertion
            return null;
        }
        else {
            return first;
        }
    }
}
