package ceylon.language.meta;

import ceylon.language.Sequential;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Method
public final class type_ {
    
    private type_() {}
    
    @TypeParameters(@TypeParameter(value = "Type", variance = Variance.OUT, satisfies = "ceylon.language::Anything"))
    @TypeInfo("ceylon.language.meta.model::Class<Type,ceylon.language::Nothing>")
    public static <Type> ceylon.language.meta.model.Class<? extends Type, ? super Sequential<? extends Object>> type(
            @Ignore TypeDescriptor $reifiedType,
            @Name("instance") @TypeInfo("Type") Type instance) {
        return (ceylon.language.meta.model.Class) Metamodel.getAppliedMetamodel(Metamodel.getTypeDescriptor(instance));
    }
}
