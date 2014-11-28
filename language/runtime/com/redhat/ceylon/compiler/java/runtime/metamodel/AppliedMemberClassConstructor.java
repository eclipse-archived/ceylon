package com.redhat.ceylon.compiler.java.runtime.metamodel;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedReference;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.Declaration;
import ceylon.language.meta.model.Class;
import ceylon.language.meta.model.Constructor;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassConstructor;

public class AppliedMemberClassConstructor<Container,Type, Arguments extends Sequential<? extends Object>> 
        implements MemberClassConstructor<Container, Type, Arguments> {

    private final FreeConstructor declaration;
    private MemberClass<Object,Type,? extends Object> container;
    
    private volatile boolean initialized = false;
    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;
    private ProducedType producedType;
    private TypeDescriptor reified$Type;
    private TypeDescriptor reified$Arguments;
    
    public AppliedMemberClassConstructor(
            TypeDescriptor reified$Type,
            TypeDescriptor reified$Arguments,
            MemberClass<Object,Type,? extends Object> container,
            ProducedType producedType,
            FreeConstructor declaration) {
        this.reified$Type = reified$Type;
        this.reified$Arguments = reified$Arguments;
        this.declaration = declaration;
        this.container = container;
        this.producedType = producedType;
    }
    
    protected void checkInit() {
        if (!this.initialized) {
            synchronized(this) {
                if (!this.initialized) {
                    init();
                    this.initialized = true;
                }
            }
        }
    }
    
    protected void init() {
        java.util.List<ProducedType> parameterProducedTypes = Metamodel.getParameterProducedTypes(declaration.constructor.getParameterLists().get(0).getParameters(), producedType);
        this.parameterTypes = Metamodel.getAppliedMetamodelSequential(parameterProducedTypes);
    }
    
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes() {
        checkInit();
        return this.parameterTypes;
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
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$() {
        return $callvariadic$(empty_.get_());
    }
    
    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(
            Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(Object arg0) {
        return $callvariadic$(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(Object arg0,
            Object arg1) {
        return $callvariadic$(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $callvariadic$(Object arg0,
            Object arg1, Object arg2) {
        return $callvariadic$(arg0, arg1, arg2, empty_.get_());
    }
    
    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $call$() {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $call$(Object instance) {
        return new AppliedConstructor<Type, Arguments>(
                reified$Type, reified$Arguments, 
                getContainer().bind(instance), 
                producedType, 
                declaration, 
                instance);
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $call$(Object arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $call$(Object arg0, Object arg1, Object arg2) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Constructor<? extends Type, ? super Arguments> $call$(Object... args) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public short $getVariadicParameterIndex$() {
        return -1;
    }
    
    ////////////////////////////////
    /*
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
     */
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
