package com.redhat.ceylon.compiler.java.language;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

public abstract class LazyInvokingIterable<Element, Absent> extends LazyIterable<Element, Absent> {
    private static final MethodType methodType = MethodType.methodType(Object.class);
    
    private final MethodHandles.Lookup lookup;
    private final Class<? extends LazyIterable> subclass = getClass();
    
    public LazyInvokingIterable(TypeDescriptor $reifiedElement,
            TypeDescriptor $reifiedAbsent, int $numMethods, boolean $spread) {
        super($reifiedElement, $reifiedAbsent, $numMethods, $spread);
        lookup = $lookup$();
    }
    // We need the (anonymous) subclass to obtain the lookup for us
    /*compiler generated*/ protected abstract MethodHandles.Lookup $lookup$();
    
    /*compiler generated*/ protected abstract Object $invoke$(MethodHandle handle) throws Throwable;
    
    @Override
    @Ignore
    protected final Object $evaluate$(int $index$) {
        java.lang.String methodName = "$"+$index$;
        Object result;
        try {
            MethodHandle handle = lookup.findSpecial(subclass, methodName, methodType, subclass);
            // by delegating to the (anonymous) subclass `this` will 
            // have the subclasses type and it can use invokeExact(), where
            // this class could not (because we can't cast or even name the type of this)
            result = $invoke$(handle);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}