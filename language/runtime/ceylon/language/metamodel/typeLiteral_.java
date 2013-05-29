package ceylon.language.metamodel;

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
    
    @TypeInfo("ceylon.language.metamodel::Type")
    @TypeParameters(@TypeParameter(value = "Type", variance = Variance.OUT, satisfies = "ceylon.language::Anything"))
    public static <Type> ceylon.language.metamodel.Type typeLiteral(@Ignore TypeDescriptor $reifiedType) {
        return Metamodel.getAppliedMetamodel($reifiedType);
    }
}
