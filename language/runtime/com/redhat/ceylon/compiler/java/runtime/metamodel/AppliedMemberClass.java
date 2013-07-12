package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.metamodel.Class;
import ceylon.language.metamodel.ClassModel$impl;
import ceylon.language.metamodel.Member$impl;
import ceylon.language.metamodel.MemberClass$impl;
import ceylon.language.metamodel.declaration.ClassDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

public class AppliedMemberClass<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedClassOrInterfaceType<Type>
    implements ceylon.language.metamodel.MemberClass<Container, Type, Arguments> {

    @Ignore
    private TypeDescriptor $reifiedContainer;
    @Ignore
    private TypeDescriptor $reifiedArguments;

    AppliedMemberClass(@Ignore TypeDescriptor $reifiedContainer,
                       @Ignore TypeDescriptor $reifiedType,
                       @Ignore TypeDescriptor $reifiedArguments, 
                       ProducedType producedType) {
        super($reifiedType, producedType);
        this.$reifiedArguments = $reifiedArguments;
        this.$reifiedContainer = $reifiedContainer;
    }

    @Override
    @Ignore
    public ClassModel$impl<Type, Arguments> $ceylon$language$metamodel$ClassModel$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Member$impl<Container, Class<? extends Type, ? super Arguments>> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public MemberClass$impl<Container, Type, Arguments> $ceylon$language$metamodel$MemberClass$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object arg0) {
        return new AppliedClassType(null, null, super.producedType, arg0);
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Class<? extends Type, ? super Arguments> $call(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex() {
        return -1;
    }
    
    @Override
    @TypeInfo("ceylon.language.metamodel.declaration::ClassDeclaration")
    public ClassDeclaration getDeclaration() {
        return (ClassDeclaration) super.getDeclaration();
    }
    
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMemberClass.class, $reifiedContainer, $reifiedType, $reifiedArguments);
    }
}
