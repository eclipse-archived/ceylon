package com.redhat.ceylon.compiler.java.runtime.metamodel;

import ceylon.language.metamodel.Member$impl;
import ceylon.language.metamodel.VariableAttribute$impl;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.ProducedTypedReference;

/**
 * Most of this class is for working around the fact that on the JVM we're not supposed to inherit the same interface twice with
 * different type arguments, so we had to override some methods with compatible return types to satisfy the compiler.
 */
public class AppliedVariableAttribute<Container, Type> 
    extends AppliedAttribute<Container, Type>
    implements ceylon.language.metamodel.VariableAttribute<Container, Type> {

    public AppliedVariableAttribute(@Ignore TypeDescriptor $reifiedContainer, @Ignore TypeDescriptor $reifiedType, 
                                    FreeAttribute declaration, ProducedTypedReference typedReference,
                                    ceylon.language.metamodel.ClassOrInterface<? extends Object> container) {
        super($reifiedContainer, $reifiedType, declaration, typedReference, container);
    }

    @Override
    @Ignore
    public VariableAttribute$impl<Container, Type> $ceylon$language$metamodel$VariableAttribute$impl() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Ignore
    public Member$impl $ceylon$language$metamodel$Member$impl() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ceylon.language.metamodel.Variable<Type> $call() {
        return (ceylon.language.metamodel.Variable<Type>)super.$call();
    }

    @Override
    public ceylon.language.metamodel.Variable<Type> $call(Object arg0) {
        return (ceylon.language.metamodel.Variable<Type>)super.$call(arg0);
    }

    @Override
    public ceylon.language.metamodel.Variable<Type> $call(Object arg0, Object arg1) {
        return (ceylon.language.metamodel.Variable<Type>)super.$call(arg0, arg1);
    }

    @Override
    public ceylon.language.metamodel.Variable<Type> $call(Object arg0, Object arg1, Object arg2) {
        return (ceylon.language.metamodel.Variable<Type>)super.$call(arg0, arg1, arg2);
    }

    @Override
    public ceylon.language.metamodel.Variable<Type> $call(Object... args) {
        return (ceylon.language.metamodel.Variable<Type>)super.$call(args);
    }

    @Override
    protected ceylon.language.metamodel.Variable<Type> bindTo(Object instance) {
        return new AppliedVariable(null, declaration, typedReference, instance);
    }
    
    @Override
    @Ignore
    public TypeDescriptor $getType() {
        return TypeDescriptor.klass(AppliedAttribute.class, super.$reifiedType, $reifiedType);
    }

}
