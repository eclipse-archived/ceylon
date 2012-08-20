package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 2)
@Method
public final class className_ {
    
    private className_() {}
    
    public static java.lang.String className(@Name("obj")
    @TypeInfo("ceylon.language.Object")
    final java.lang.Object object) {
    	//TODO: type args?
        java.lang.String name = object.getClass().getName();
        int i = name.indexOf("$");
        if (i>0) {
            name = name.substring(0, i) + 
                    name.substring(i+1);
        }
        if (name.endsWith("_") 
                && object.getClass().getAnnotation(Ceylon.class) != null) {
            name = name.substring(0, name.length()-1);
        }
        return name;
    }
}
