package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Method
public final class className_ {
    
    private className_() {}
    
    public static java.lang.String className(@Name("obj")
    @TypeInfo("ceylon.language.Object")
    final java.lang.Object object) {
    	//TODO: type args?
        Name annot = object.getClass().getAnnotation(Name.class);
        if (annot != null) {
            if (object.getClass().getPackage() != null) {
                return object.getClass().getPackage().getName() + "." + annot.value();
            } else {
                return annot.value();
            }
        } else {
            java.lang.String name = object.getClass().getName();
            // FIXME The next part really shouldn't be necessary as long as we add @Name
            // annotations to all classes with mangled names
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
}
