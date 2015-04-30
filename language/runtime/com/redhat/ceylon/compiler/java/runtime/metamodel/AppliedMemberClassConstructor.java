package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.declaration.Declaration;
import ceylon.language.meta.model.Constructor;
import ceylon.language.meta.model.MemberClass;
import ceylon.language.meta.model.MemberClassConstructor;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.ProducedType;

public class AppliedMemberClassConstructor<Container,Type, Arguments extends Sequential<? extends Object>> 
        implements MemberClassConstructor<Container, Type, Arguments>, ReifiedType {

    private final FreeConstructor declaration;
    private final MemberClass<Object,Type,? extends Object> container;
    private final ProducedType producedType;
    private final TypeDescriptor reified$Type;
    private final TypeDescriptor reified$Arguments;
    private final TypeDescriptor reified$Container;
    
    private volatile boolean initialized = false;
    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;
    
    public AppliedMemberClassConstructor(
            TypeDescriptor reified$Container,
            TypeDescriptor reified$Type,
            TypeDescriptor reified$Arguments,
            MemberClass<Object,Type,? extends Object> container,
            ProducedType producedType,
            FreeConstructor declaration) {
        this.reified$Container = reified$Container;
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
    public Constructor<? extends Type, ? super Arguments> bind(Object container) {
        return (Constructor<? extends Type, ? super Arguments>) Metamodel.bind(this, this.producedType.getQualifyingType().getQualifyingType(), container);
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

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedMemberClassConstructor.class, reified$Container, reified$Type, reified$Arguments);
    }
}
