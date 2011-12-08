package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Method;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

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
