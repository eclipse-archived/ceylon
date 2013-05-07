package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Formal$annotation;
import ceylon.language.Shared$annotation;
import ceylon.language.metamodel.ClassOrInterface;
import ceylon.language.metamodel.Member$impl;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type"),
    @TypeParameter(value = "Kind", satisfies = "ceylon.language.metamodel::Declaration")
})
public abstract class AppliedMember<Type, Declaration extends ceylon.language.metamodel.Declaration> 
    implements ceylon.language.metamodel.Member<Type, Declaration>, ReifiedType {

    private AppliedClassOrInterfaceType<Type> container;
    @Ignore
    private TypeDescriptor $reifiedDeclaration;
    @Ignore
    private TypeDescriptor $reifiedType;

    public AppliedMember(@Ignore TypeDescriptor $reifiedType, @Ignore TypeDescriptor $reifiedDeclaration,
                         AppliedClassOrInterfaceType<Type> container){
        this.$reifiedType = $reifiedType;
        this.$reifiedDeclaration = $reifiedDeclaration;
        this.container = container;
    }
    
    @Override
    @Ignore
    public Member$impl<Type, Declaration> $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Shared$annotation
    @Formal$annotation
    @TypeInfo("ceylon.language.metamodel::ClassOrInterface<Type>")
    public ClassOrInterface<? extends Type> getDeclaringClassOrInterface() {
        return container;
    }

    @Override
    public Declaration $call() {
        throw new UnsupportedOperationException();
    }

    protected abstract Declaration bindTo(Object instance);
    
    @Override
    public Declaration $call(Object instance) {
        return bindTo(instance);
    }

    @Override
    public Declaration $call(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Declaration $call(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Declaration $call(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short $getVariadicParameterIndex() {
        return -1;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMember.class, $reifiedType, $reifiedDeclaration);
    }
}
