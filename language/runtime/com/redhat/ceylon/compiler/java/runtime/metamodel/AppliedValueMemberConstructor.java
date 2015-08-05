package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassValueConstructor;
import ceylon.language.meta.model.ValueConstructor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor.Nothing;
import com.redhat.ceylon.model.typechecker.model.TypedReference;


@Ceylon(major=8)
@com.redhat.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::MemberClassValueConstructor<Container,Type,Set>")
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Set", variance = Variance.IN)
})
public class AppliedValueMemberConstructor<Container, Type, Set>
        extends AppliedAttribute<Container, Type, Set> 
        implements MemberClassValueConstructor<Container, Type, Set>{

    final AppliedMemberClass<Container, Type, ?> clazz;
    
    public AppliedValueMemberConstructor(TypeDescriptor $reifiedContainer,
            TypeDescriptor $reifiedGet,
            TypeDescriptor $reifiedSet,
            FreeValueConstructor declaration, TypedReference typedReference,
            AppliedMemberClass<Container, Type, ?> clazz) {
        super($reifiedContainer, $reifiedGet, Nothing.NothingType, declaration, typedReference,
                clazz.getContainer());
        this.clazz = clazz;
    }
    
    @Override
    public MemberClass<Container, Type, ?> getType() {
        return clazz;
    }
    
    @Override
    public ceylon.language.meta.model.Class<Type, ?> getContainer() {
        return (ceylon.language.meta.model.Class)clazz.getContainer();
    }
    
    public ValueConstructorDeclaration getDeclaration() {
        return (ValueConstructorDeclaration)declaration;
    }
    
    @TypeInfo("ceylon.language.meta.model::ValueConstructor<Type,Set>")
    @Override
    public ValueConstructor<Type, Set> bind(Object instance) {
        return null;
    }
}