package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Method
public final class className {
    
    private className() {
    }
    
    public static java.lang.String className(@Name("obj")
    @TypeInfo("ceylon.language.Object")
    final java.lang.Object object) {
    	//TODO: type args?
        return object.getClass().getName();
    }
}
