package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.model.Function;
import ceylon.language.model.FunctionModel$impl;
import ceylon.language.model.Method$impl;
import ceylon.language.model.Model$impl;
import ceylon.language.model.declaration.FunctionDeclaration;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Container", variance = Variance.IN),
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
})
public class AppliedMethod<Container, Type, Arguments extends Sequential<? extends Object>> 
    extends AppliedMember<Container, ceylon.language.model.Function<? extends Type, ? super Arguments>> 
    implements ceylon.language.model.Method<Container, Type, Arguments> {

    private FreeFunction declaration;
    private ProducedTypedReference appliedFunction;
    private ceylon.language.model.Type<Type> closedType;
    @Ignore
    private TypeDescriptor $reifiedType;
    @Ignore
    private TypeDescriptor $reifiedArguments;
    
    private Map<? extends ceylon.language.model.declaration.TypeParameter, ? extends ceylon.language.model.Type<?>> typeArguments;

    public AppliedMethod(@Ignore TypeDescriptor $reifiedContainer, 
                         @Ignore TypeDescriptor $reifiedType, 
                         @Ignore TypeDescriptor $reifiedArguments, 
                         ProducedTypedReference appliedFunction, 
                         FreeFunction declaration,
                         ceylon.language.model.Type<? extends Object> container) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.model.Function.class, $reifiedType, $reifiedArguments), container);
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
        this.appliedFunction = appliedFunction;
        this.declaration = declaration;
        this.typeArguments = Metamodel.getTypeArguments(declaration, appliedFunction);
        this.closedType = Metamodel.getAppliedMetamodel(Metamodel.getFunctionReturnType(appliedFunction));
    }

    @Override
    @Ignore
    public ceylon.language.model.Generic$impl $ceylon$language$model$Generic$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$model$Model$impl() {
        return null;
    }

    @Override
    @Ignore
    public Method$impl<Container, Type, Arguments> $ceylon$language$model$Method$impl() {
        return null;
    }

    @Override
    @Ignore
    public FunctionModel$impl<Type, Arguments> $ceylon$language$model$FunctionModel$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.model.declaration::TypeParameter,ceylon.language.model::Type<ceylon.language::Anything>>")
    public ceylon.language.Map<? extends ceylon.language.model.declaration.TypeParameter, ? extends ceylon.language.model.Type<?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.model.declaration::FunctionDeclaration")
    public FunctionDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<Type>")
    public ceylon.language.model.Type<? extends Type> getType() {
        return closedType;
    }

    @Override
    protected Function<Type, Arguments> bindTo(Object instance) {
        return new AppliedFunction<Type, Arguments>($reifiedType, $reifiedArguments, appliedFunction, declaration, getContainer(), instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMethod.class, super.$reifiedType, $reifiedType, $reifiedArguments);
    }
    
    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic() {
        return $call$variadic(empty_.$get());
    }
    
    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object... argsAndVarargs) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0) {
        return $call$variadic(arg0, empty_.$get());
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1) {
        return $call$variadic(arg0, arg1, empty_.$get());
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1, Object arg2) {
        return $call$variadic(arg0, arg1, arg2, empty_.$get());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringClassOrInterface().hashCode();
        result = 37 * result + getDeclaration().hashCode();
        result = 37 * result + getTypeArguments().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(obj == this)
            return true;
        if(obj instanceof ceylon.language.model.Method == false)
            return false;
        ceylon.language.model.Method<?, ?, ?> other = (ceylon.language.model.Method<?, ?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringClassOrInterface().equals(other.getDeclaringClassOrInterface())
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    @TypeInfo("ceylon.language.model::Type<ceylon.language::Anything>")
    public ceylon.language.model.Type<? extends java.lang.Object> getContainer(){
        return getDeclaringClassOrInterface();
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }
}
