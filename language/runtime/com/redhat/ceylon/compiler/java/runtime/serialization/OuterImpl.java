package com.redhat.ceylon.compiler.java.runtime.serialization;

import ceylon.language.serialization.Outer;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Implementation of ceylon.language.serialization.Outer
 * 
 * This has to be implemented in Java because it needs to call
 * {@link Serializable#$get$(ceylon.language.serialization.ReachableReference)},
 * whose name is not permitted in Ceylon, 
 * but must be illegal so it cannot collide with a user class member 
 */
@Ceylon(major=8, minor=0)
@Class(identifiable=false)
public class OuterImpl implements Outer, ReifiedType {
    
    public static final OuterImpl instance = new OuterImpl(); 
    
    private static final TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(OuterImpl.class);
    
    private OuterImpl() {
    }
    
    @TypeInfo("ceylon.language::Object")
    @Override
    public java.lang.Object referred(
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object instance) {
        return ((Serializable)instance).$get$(this);
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }

    @Override
    public String toString() {
        return "Outer";
    }
}