package com.redhat.ceylon.compiler.java.runtime.metamodel;

import java.lang.invoke.MethodHandle;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.Callable;
import ceylon.language.Sequential;

@Ceylon(major = 5)
@com.redhat.ceylon.compiler.java.metadata.Class
@TypeParameters({
    @TypeParameter(value = "Type", variance = Variance.OUT),
    @TypeParameter(value = "Arguments", variance = Variance.IN, satisfies = "ceylon.language::Sequential<ceylon.language::Anything>"),
    })
@SatisfiedTypes("ceylon.language::Callable<Type,Arguments>")
public class BoundFunction<Type, Arguments extends Sequential<? extends Object>> implements Callable<Type>, ReifiedType {

    @Ignore
    private TypeDescriptor $reifiedArguments;
    @Ignore
    private TypeDescriptor $reifiedType;
    private MethodHandle method;

    public BoundFunction(@Ignore TypeDescriptor $reifiedType, @Ignore TypeDescriptor $reifiedArguments, MethodHandle method) {
        this.method = method;
        this.$reifiedType = $reifiedType;
        this.$reifiedArguments = $reifiedArguments;
    }

    @Override
    public Type $call() {
        try {
            return (Type)method.invokeExact();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke bound method", e);
        }
    }

    @Override
    public Type $call(Object arg0) {
        try {
            return (Type)method.invokeExact(arg0);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke bound method", e);
        }
    }

    @Override
    public Type $call(Object arg0, Object arg1) {
        try {
            return (Type)method.invokeExact(arg0, arg1);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke bound method", e);
        }
    }

    @Override
    public Type $call(Object arg0, Object arg1, Object arg2) {
        try {
            return (Type)method.invokeExact(arg0, arg1, arg2);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke bound method", e);
        }
    }

    @Override
    public Type $call(Object... args) {
        try {
            // FIXME: this does not do invokeExact and does boxing/widening
            return (Type)method.invokeWithArguments(args);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to invoke bound method", e);
        }
    }

    @Override
    public short $getVariadicParameterIndex() {
        // TODO Auto-generated method stub
        return -1;
    }

    @Override
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(BoundFunction.class, $reifiedType, $reifiedArguments);
    }

}
