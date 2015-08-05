package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.ValueConstructor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.Nothing;
import com.redhat.ceylon.model.typechecker.model.TypedReference;


@Ceylon(major=8)
@com.redhat.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::ValueConstructor<Type,Set>")
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Set", variance = Variance.IN, defaultValue="ceylon.language::Nothing")
})
public class AppliedValueConstructor<Type,Set> 
        extends AppliedValue<Type, Set> 
        implements ValueConstructor<Type,Set> {

    final AppliedClass<Type,?> clazz;
    
    public AppliedValueConstructor(TypeDescriptor $reifiedGet,
            TypeDescriptor $reifiedSet,
            FreeValueConstructor value,
            TypedReference valueTypedReference,
            AppliedClass<Type,?> clazz, Object instance) {
        super($reifiedGet, Nothing.NothingType, value, valueTypedReference, null, instance);
        this.clazz = clazz;
    }

    @Override
    public ceylon.language.meta.model.Class<Type,?> getType() {
        return clazz;
    }
    
    @Override
    public ceylon.language.meta.model.Class<?,?> getContainer() {
        return null;
    }
    
    @Override
    public ceylon.language.meta.declaration.ValueConstructorDeclaration getDeclaration() {
        return (ValueConstructorDeclaration)declaration;
    }

}