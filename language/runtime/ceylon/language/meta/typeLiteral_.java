package ceylon.language.meta;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@Method
public final class typeLiteral_ {
    
    private typeLiteral_() {}
    
    @SuppressWarnings({ "unchecked", "hiding" })
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    @TypeParameters(@TypeParameter(value = "Type", variance = Variance.OUT, satisfies = "ceylon.language::Anything"))
    public static <Type> ceylon.language.meta.model.Type<? extends Type> typeLiteral(@Ignore TypeDescriptor $reifiedType) {
        return (ceylon.language.meta.model.Type<? extends Type>) Metamodel.getAppliedMetamodel($reifiedType);
    }
}
