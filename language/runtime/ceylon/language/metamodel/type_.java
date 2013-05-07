package ceylon.language.metamodel;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major = 5)
@Method
public final class type_ {
    
    private type_() {}
    
    @TypeInfo("ceylon.language.metamodel::AppliedType")
    public static AppliedType type(@Name("instance")
        @TypeInfo("ceylon.language::Anything")
        final Object instance) {
        return Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(instance));
    }
}
