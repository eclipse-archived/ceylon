package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.Sequential;
import ceylon.language.model.Member$impl;
import ceylon.language.model.VariableAttribute$impl;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

/**
 * Most of this class is for working around the fact that on the JVM we're not supposed to inherit the same interface twice with
 * different type arguments, so we had to override some methods with compatible return types to satisfy the compiler.
 */
public class AppliedVariableAttribute<Container, Type> 
    extends AppliedAttribute<Container, Type>
    implements ceylon.language.model.VariableAttribute<Container, Type> {

    public AppliedVariableAttribute(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor $reifiedType, 
                                    FreeAttribute declaration, ProducedTypedReference typedReference,
                                    ceylon.language.model.ClassOrInterface<? extends Object> container) {
        super($reifiedContainer, $reifiedType, declaration, typedReference, container);
    }

    @Override
    @Ignore
    public VariableAttribute$impl<Container, Type> $ceylon$language$model$VariableAttribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Member$impl $ceylon$language$model$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic() {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic();
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object arg0) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object arg0, Object arg1) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object arg0, Object arg1, Object arg2) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object... args) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(args);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Sequential<?> varargs) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(varargs);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object arg0, Sequential<?> varargs) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(arg0, varargs);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object arg0, Object arg1, Sequential<?> varargs) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(arg0, arg1, varargs);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call$variadic(Object arg0, Object arg1, Object arg2, Sequential<?> varargs) {
        return (ceylon.language.model.Variable<Type>)super.$call$variadic(arg0, arg1, arg2, varargs);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call() {
        return (ceylon.language.model.Variable<Type>)super.$call();
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object arg0) {
        return (ceylon.language.model.Variable<Type>)super.$call(arg0);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object arg0, Object arg1) {
        return (ceylon.language.model.Variable<Type>)super.$call(arg0, arg1);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object arg0, Object arg1, Object arg2) {
        return (ceylon.language.model.Variable<Type>)super.$call(arg0, arg1, arg2);
    }

    @Override
    @Ignore
    public ceylon.language.model.Variable<Type> $call(Object... args) {
        return (ceylon.language.model.Variable<Type>)super.$call(args);
    }

    @Override
    protected ceylon.language.model.Variable<Type> bindTo(Object instance) {
        return new AppliedVariable(null, declaration, typedReference, getContainer(), instance);
    }

    @Override
    public int hashCode() {
        // this one's fine
        return super.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        // this one's fine
        return super.equals(obj);
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedAttribute.class, super.$reifiedType, $reifiedType);
    }

}
