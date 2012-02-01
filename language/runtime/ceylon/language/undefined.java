package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class undefined {
    
    private undefined() {
    }
    
    @TypeInfo("ceylon.language.Boolean")
    public static boolean undefined(@Name("value")
    @TypeInfo("ceylon.language.Float")
    double value) {
        return Double.isNaN(value);
    }
}
