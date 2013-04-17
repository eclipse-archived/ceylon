package ceylon.language.metamodel;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major = 4)
@Method
public final class type_ {
    
    private type_() {}
    
    @TypeInfo("ceylon.language.metamodel::ProducedType")
    public static ProducedType type(@Name("instance")
        @TypeInfo("ceylon.language::Anything")
        final Object instance) {
        return Metamodel.getMetamodel(Metamodel.getTypeDescriptor(instance));
    }
}
