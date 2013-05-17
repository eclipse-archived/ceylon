package ceylon.language.metamodel;

import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;

@Ceylon(major = 5)
@Method
public final class type_ {
    
    private type_() {}
    
    @TypeInfo("ceylon.language.metamodel::Class<ceylon.language::Anything,ceylon.language::Nothing>")
    public static ceylon.language.metamodel.Class<? extends Object, ? super Sequential<? extends Object>> type(@Name("instance")
        @TypeInfo("ceylon.language::Anything")
        final Object instance) {
        return (ceylon.language.metamodel.Class) Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(instance));
    }
}
