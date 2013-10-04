package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Map;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.meta.model.Function;
import ceylon.language.meta.model.FunctionModel$impl;
import ceylon.language.meta.model.Method$impl;
import ceylon.language.meta.model.Model$impl;
import ceylon.language.meta.declaration.FunctionDeclaration;

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
    extends AppliedMember<Container, ceylon.language.meta.model.Function<? extends Type, ? super Arguments>> 
    implements ceylon.language.meta.model.Method<Container, Type, Arguments> {

    private FreeFunction declaration;
    private ProducedTypedReference appliedFunction;
    private ceylon.language.meta.model.Type<Type> closedType;
    @Ignore
    private TypeDescriptor $reifiedType;
    @Ignore
    private TypeDescriptor $reifiedArguments;
    
    private Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> typeArguments;

    public AppliedMethod(@Ignore TypeDescriptor $reifiedContainer, 
                         @Ignore TypeDescriptor $reifiedType, 
                         @Ignore TypeDescriptor $reifiedArguments, 
                         ProducedTypedReference appliedFunction, 
                         FreeFunction declaration,
                         ceylon.language.meta.model.Type<? extends Object> container) {
        super($reifiedContainer, TypeDescriptor.klass(ceylon.language.meta.model.Function.class, $reifiedType, $reifiedArguments), container);
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
        this.appliedFunction = appliedFunction;
        this.declaration = declaration;
        this.typeArguments = Metamodel.getTypeArguments(declaration, appliedFunction);
        this.closedType = Metamodel.getAppliedMetamodel(Metamodel.getFunctionReturnType(appliedFunction));
    }

    @Override
    @Ignore
    public ceylon.language.meta.model.Generic$impl $ceylon$language$meta$model$Generic$impl() {
        return null;
    }

    @Override
    @Ignore
    public Model$impl $ceylon$language$meta$model$Model$impl() {
        return null;
    }

    @Override
    @Ignore
    public Method$impl<Container, Type, Arguments> $ceylon$language$meta$model$Method$impl() {
        return null;
    }

    @Override
    @Ignore
    public FunctionModel$impl<Type, Arguments> $ceylon$language$meta$model$FunctionModel$impl() {
        return null;
    }

    @Override
    @TypeInfo("ceylon.language::Map<ceylon.language.meta.declaration::TypeParameter,ceylon.language.meta.model::Type<ceylon.language::Anything>>")
    public ceylon.language.Map<? extends ceylon.language.meta.declaration.TypeParameter, ? extends ceylon.language.meta.model.Type<?>> getTypeArguments() {
        return typeArguments;
    }

    @Override
    @TypeInfo("ceylon.language.meta.declaration::FunctionDeclaration")
    public FunctionDeclaration getDeclaration() {
        return declaration;
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<Type>")
    public ceylon.language.meta.model.Type<? extends Type> getType() {
        return closedType;
    }

    @Override
    protected Function<Type, Arguments> bindTo(Object instance) {
        return new AppliedFunction<Type, Arguments>($reifiedType, $reifiedArguments, appliedFunction, declaration, getContainer(), instance);
    }

    @Ignore
    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedMethod.class, super.$reifiedContainer, $reifiedType, $reifiedArguments);
    }
    
    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic() {
        return $call$variadic(empty_.get_());
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
        return $call$variadic(arg0, empty_.get_());
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1) {
        return $call$variadic(arg0, arg1, empty_.get_());
    }

    @Override
    @Ignore
    public Function<? extends Type, ? super Arguments> $call$variadic(
            Object arg0, Object arg1, Object arg2) {
        return $call$variadic(arg0, arg1, arg2, empty_.get_());
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 37 * result + getDeclaringType().hashCode();
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
        if(obj instanceof ceylon.language.meta.model.Method == false)
            return false;
        ceylon.language.meta.model.Method<?, ?, ?> other = (ceylon.language.meta.model.Method<?, ?, ?>) obj;
        return getDeclaration().equals(other.getDeclaration())
                && getDeclaringType().equals(other.getDeclaringType())
                && getTypeArguments().equals(other.getTypeArguments());
    }

    @Override
    @TypeInfo("ceylon.language.meta.model::Type<ceylon.language::Anything>")
    public ceylon.language.meta.model.Type<? extends java.lang.Object> getContainer(){
        return getDeclaringType();
    }

    @Override
    public String toString() {
        return Metamodel.toTypeString(this);
    }
}
