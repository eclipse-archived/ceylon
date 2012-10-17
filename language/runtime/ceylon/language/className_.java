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
    @TypeInfo("ceylon.language::Object")
    final java.lang.Object object) {
    	//TODO: type args?
        Name annot = object.getClass().getAnnotation(Name.class);
        if (annot != null) {
            if (object.getClass().getPackage() != null) {
                return object.getClass().getPackage().getName() + "::" + annot.value();
            } else {
                return annot.value();
            }
        } else {
            Ceylon ann = object.getClass().getAnnotation(Ceylon.class);
            java.lang.String pkg = object.getClass().getPackage() == null ? "" : object.getClass().getPackage().getName();
            java.lang.String name = object.getClass().getSimpleName();
            if (name.isEmpty()) {
                name = object.getClass().getName();
                //Remove the package from the classname
                if (!pkg.isEmpty()) {
                    name = name.substring(pkg.length()+1);
                }
            }
            // FIXME The mangle part really shouldn't be necessary as long as we add @Name
            // annotations to all classes with mangled names
            return mangle(pkg, ann) + "::" + mangle(name, ann);
        }
    }

    private final static java.lang.String mangle(java.lang.String s, Ceylon ann) {
        int i = s.indexOf("$");
        if (i>0) {
            s = s.substring(0, i) + s.substring(i+1);
        }
        if (s.endsWith("_") && ann != null) {
            s = s.substring(0, s.length()-1);
        }
        return s;
    }

}
