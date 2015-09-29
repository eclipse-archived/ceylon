package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.TypeParameter;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.Class;
import ceylon.language.meta.model.ClassModel;

public class AppliedInitializer<Type, Arguments extends Sequential<? extends Object>> implements CallableConstructor<Type, Arguments>{

    private Class<Type, Arguments> clazz;

    public AppliedInitializer(Class<Type, Arguments> clazz) {
        this.clazz = clazz;
    }
    
    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getTypeArgumentList() {
        return clazz.getTypeArgumentList();
    }

    @Override
    public Sequential<? extends Sequence<? extends Object>> getTypeArgumentWithVarianceList() {
        return clazz.getTypeArgumentWithVarianceList();
    }

    @Override
    public Map<? extends TypeParameter, ? extends Sequence<? extends Object>> getTypeArgumentWithVariances() {
        return clazz.getTypeArgumentWithVariances();
    }

    @Override
    public Map<? extends TypeParameter, ? extends ceylon.language.meta.model.Type<? extends Object>> getTypeArguments() {
        return clazz.getTypeArguments();
    }

    @Override
    public Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> getParameterTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Type apply() {
        return clazz.apply();
    }

    @Override
    public Type apply(Sequential<? extends Object> arg0) {
        return clazz.apply(arg0);
    }

    @Override
    public Type namedApply(
            Iterable<? extends Entry<? extends String, ? extends Object>, ? extends Object> arg0) {
        return clazz.namedApply(arg0);
    }

    @Override
    public Type $call$() {
        return clazz.$call$();
    }

    @Override
    public Type $callvariadic$() {
        return clazz.$callvariadic$();
    }

    @Override
    public Type $callvariadic$(Sequential<?> varargs) {
        return clazz.$callvariadic$(varargs);
    }

    @Override
    public Type $call$(Object arg0) {
        return clazz.$call$(arg0);
    }

    @Override
    public Type $callvariadic$(Object arg0) {
        return clazz.$callvariadic$(arg0);
    }

    @Override
    public Type $callvariadic$(Object arg0, Sequential<?> varargs) {
        return clazz.$callvariadic$(arg0, varargs);
    }

    @Override
    public Type $call$(Object arg0, Object arg1) {
        return clazz.$call$(arg0, arg1);
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1) {
        return clazz.$callvariadic$(arg0, arg1);
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Sequential<?> varargs) {
        return clazz.$callvariadic$(arg0, arg1, varargs);
    }

    @Override
    public Type $call$(Object arg0, Object arg1, Object arg2) {
        return clazz.$call$(arg0, arg1, arg2);
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2) {
        return clazz.$callvariadic$(arg0, arg1, arg2);
    }

    @Override
    public Type $callvariadic$(Object arg0, Object arg1, Object arg2,
            Sequential<?> varargs) {
        return clazz.$callvariadic$(arg0, arg1, arg2, varargs);
    }

    @Override
    public Type $call$(Object... args) {
        return clazz.$call$(args);
    }

    @Override
    public Type $callvariadic$(Object... argsAndVarargs) {
        return clazz.$callvariadic$(argsAndVarargs);
    }

    @Override
    public short $getVariadicParameterIndex$() {
        return clazz.$getVariadicParameterIndex$();
    }

    @Override
    public ClassModel getContainer() {
        return clazz;
    }

    @Override
    public CallableConstructorDeclaration getDeclaration() {
        return (CallableConstructorDeclaration)clazz.getDefaultConstructor();
    }

    @Override
    public ClassModel getType() {
        return clazz;
    }

}
