package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 7)
@Method
public final class className_ {
    
    private className_() {}
    
    public static java.lang.String className(@Name("obj")
    @TypeInfo("ceylon.language::Object")
    final java.lang.Object object) {
        return object.getClass().getName();
    }
    
}
