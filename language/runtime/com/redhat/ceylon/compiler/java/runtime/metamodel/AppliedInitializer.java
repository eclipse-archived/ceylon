package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.util.List;

import ceylon.language.Entry;
import ceylon.language.Iterable;
import ceylon.language.Map;
import ceylon.language.Sequence;
import ceylon.language.Sequential;
import ceylon.language.String;
import ceylon.language.meta.declaration.CallableConstructorDeclaration;
import ceylon.language.meta.declaration.TypeParameter;
import ceylon.language.meta.model.CallableConstructor;
import ceylon.language.meta.model.ClassModel;

import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.model.typechecker.model.Parameter;

public class AppliedInitializer<Type, Arguments extends Sequential<? extends Object>> 
        implements CallableConstructor<Type, Arguments>, ReifiedType {

    private AppliedClass<Type, Arguments> clazz;
    private List<com.redhat.ceylon.model.typechecker.model.Type> parameterProducedTypes;
    private Sequential<? extends ceylon.language.meta.model.Type<? extends Object>> parameterTypes;

    public AppliedInitializer(AppliedClass<Type, Arguments> clazz) {
        this.clazz = clazz;
        List<Parameter> parameters = ((com.redhat.ceylon.model.typechecker.model.Class)clazz.declaration.declaration).getParameterLists().get(0).getParameters();
        /*this.firstDefaulted = Metamodel.getFirstDefaultedParameter(parameters);
        this.variadicIndex = Metamodel.getVariadicParameter(parameters);

        Object[] defaultedMethods = null;
        if(firstDefaulted != -1){
            // if we have 2 params and first is defaulted we need 2 + 1 - 0 = 3 methods:
            // f(), f(a) and f(a, b)
            this.dispatch = new MethodHandle[parameters.size() + 1 - firstDefaulted];
            defaultedMethods = new Object[dispatch.length];
        }*/

        // get a list of produced parameter types
        this.parameterProducedTypes = Metamodel.getParameterProducedTypes(parameters, clazz.producedType);
        this.parameterTypes = Metamodel.getAppliedMetamodelSequential(this.parameterProducedTypes);
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
        return parameterTypes;
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
    public ClassModel<?,?> getContainer() {
        return clazz;
    }

    @Override
    public CallableConstructorDeclaration getDeclaration() {
        return ((FreeClass)clazz.declaration).getDefaultConstructor();
    }

    @Override
    public ClassModel<?,?> getType() {
        return clazz;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AppliedInitializer<?,?> other = (AppliedInitializer<?,?>) obj;
        if (clazz == null) {
            if (other.clazz != null)
                return false;
        } else if (!clazz.equals(other.clazz))
            return false;
        return true;
    }

    public java.lang.String toString() {
        return clazz.toString();
    }

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(AppliedInitializer.class, clazz.$reifiedType, clazz.$reifiedArguments);
    }
}
