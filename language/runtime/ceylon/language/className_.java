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
        //return name(object.getClass());
    }
    
    /*private static java.lang.String name(Class<?> c) {
        Name annot = c.getAnnotation(Name.class);
        java.lang.String cn;
        if (annot == null) {
            cn = mangle(c.getSimpleName(), 
                    c.isAnnotationPresent(Ceylon.class));
        } 
        else {
            cn = annot.value();
        }
        if (c.isLocalClass()) {
            return cn;
        }
        else if (c.isMemberClass()) {
            return name(c.getEnclosingClass()) + '.' + cn;
        }
        else {
            return mangle(c.getPackage().getName(), 
                    c.isAnnotationPresent(Ceylon.class)) + 
                "::" + cn;
        }
    }

    private final static java.lang.String mangle(java.lang.String s, 
            boolean ceylon) {
        int i = s.indexOf("$");
        if (i>0) {
            s = s.substring(0, i) + s.substring(i+1);
        }
        if (s.endsWith("_") && ceylon) {
            s = s.substring(0, s.length()-1);
        }
        return s;
    }*/

}
