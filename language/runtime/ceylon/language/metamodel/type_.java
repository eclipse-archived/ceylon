package ceylon.language.metamodel;

import com.redhat.ceylon.compiler.java.Util;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 4)
@Method
public final class type_ {
    
    private type_() {}
    
    @TypeInfo("ceylon.language::Declaration")
    public static Declaration type(@Name("instance")
        @TypeInfo("ceylon.language::Anything")
        final Object instance) {
        return Util.getMetamodel(Util.getTypeDescriptor(instance));
    }
}
