package com.redhat.ceylon.compiler.java.runtime.metamodel;

import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;

import ceylon.language.Sequential;
import ceylon.language.meta.declaration.Declaration;
import ceylon.language.meta.model.Constructor;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassConstructor;

public class AppliedMemberClassConstructor<Container,Type, Arguments extends Sequential<? extends Object>> 
        implements MemberClassConstructor<Container, Type, Arguments> {

    private final FreeConstructor declaration;
    private MemberClass<Object,Type,? extends Object> container;
    
    public AppliedMemberClassConstructor(MemberClass<Object,Type,? extends Object> container,
            ProducedReference r,
            FreeConstructor declaration) {
        this.declaration = declaration;
        this.container = container;
    }
    
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Declaration getDeclaration() {
        return declaration;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> bind(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @TypeInfo("ceylon.language.meta.model::MemberClass<ceylon.language::Nothing,Type,ceylon.language::Nothing>")
    @Override
    public MemberClass<Object,Type,? extends Object> getContainer() {
        return container;
    }
    
    @Override
    public Constructor<? extends Type, ? super Arguments> $call$() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $call$(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $call$(Object arg0,
            Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $call$(Object arg0,
            Object arg1, Object arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $call$(Object... args) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object... argsAndVarargs) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public short $getVariadicParameterIndex$() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public java.lang.String toString() {
        return Metamodel.toTypeString(getContainer()) + "." + getDeclaration().getName();
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getContainer().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.meta.model.MemberClass == false)
            return false;
        ceylon.language.meta.model.MemberClassConstructor<?, ?, ?> other = (ceylon.language.meta.model.MemberClassConstructor<?, ?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getContainer().equals(other.getContainer());
    }
}
