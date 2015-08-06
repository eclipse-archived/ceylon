package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.ValueConstructorDeclaration;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassCallableConstructor;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Reference;
import com.redhat.ceylon.model.typechecker.model.TypedReference;


@Ceylon(major=8)
@com.redhat.ceylon.compiler.java.metadata.Class
@SatisfiedTypes("ceylon.language.meta.model::Method<Container,Type,Arguments>")
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN)
})
public class AppliedCallableMemberConstructor<Container, Type, Arguments extends Sequential<? extends Object>>
        extends AppliedMethod<Container, Type, Arguments> 
        implements MemberClassCallableConstructor<Container, Type, Arguments>{

    final AppliedMemberClass<Container, Type, ?> clazz;
    
    public AppliedCallableMemberConstructor(TypeDescriptor $reifiedContainer,
            TypeDescriptor $reifiedType, TypeDescriptor $reifiedArguments,
            Reference appliedFunction, FreeCallableConstructor declaration,
            AppliedMemberClass<Container, Type, ?> clazz) {
        super($reifiedContainer, $reifiedType, $reifiedArguments, appliedFunction,
                declaration, clazz.getContainer());
        this.clazz = clazz;
    }
    
    @Override
    public MemberClass<Container, Type, ?> getType() {
        return clazz;
    }
    
    @Override
    public MemberClass<Container, Type, ?> getContainer() {
        return clazz;
    }
    
    @Override
    public CallableConstructorDeclaration getDeclaration() {
        return (CallableConstructorDeclaration)declaration;
    }
    
    @Override
    public CallableConstructor<Type, Arguments> bind(Object instance) {
        return null;
    }
}