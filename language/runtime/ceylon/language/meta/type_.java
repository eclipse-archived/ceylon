package ceylon.language.meta;

import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getAppliedMetamodel;
import static com.redhat.ceylon.compiler.java.runtime.metamodel.Metamodel.getTypeDescriptor;
import ceylon.language.Sequential;
import ceylon.language.meta.model.ClassModel;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7)
@Method
public final class type_ {
    
    private type_() {}
    
    @SuppressWarnings("unchecked")
    @TypeInfo("ceylon.language.meta.model::ClassModel<Type,ceylon.language::Nothing>")
    @TypeParameters(@TypeParameter(value = "Type", 
    		variance = Variance.OUT, 
    		satisfies = "ceylon.language::Anything"))
    public static <Type> 
    ClassModel<? extends Type, ? super Sequential<? extends Object>>
    type(@Ignore TypeDescriptor $reifiedType,
            @Name("instance") @TypeInfo("Type") Type instance) {
        return (ClassModel<? extends Type, ? super Sequential<? extends Object>>) 
        		getAppliedMetamodel(getTypeDescriptor(instance));
    }
    
}
