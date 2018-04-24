/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.language;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.eclipse.ceylon.compiler.java.metadata.Ignore;
import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.language.LazyIterable;

public abstract class LazyInvokingIterable<Element, Absent> 
extends LazyIterable<Element, Absent> {
    
    private static final MethodType methodType = 
            MethodType.methodType(Object.class);
    
    private final MethodHandles.Lookup lookup;
    
    @SuppressWarnings("rawtypes")
    private final Class<? extends LazyIterable> subclass = 
            getClass();
    
    public LazyInvokingIterable(
            TypeDescriptor $reifiedElement,
            TypeDescriptor $reifiedAbsent, 
            int $numMethods, boolean $spread) {
        super($reifiedElement, $reifiedAbsent, 
                $numMethods, $spread);
        lookup = $lookup$();
    }
    
    // We need the (anonymous) subclass to obtain the lookup for us
    /*compiler generated*/ 
    protected abstract MethodHandles.Lookup $lookup$();
    
    /*compiler generated*/ 
    protected abstract Object $invoke$(MethodHandle handle) 
            throws Throwable;
    
    @Override
    @Ignore
    protected final Object $evaluate$(int $index$) {
        java.lang.String methodName = "$"+$index$;
        try {
            MethodHandle handle = lookup.findSpecial(subclass, 
                    methodName, methodType, subclass);
            // by delegating to the (anonymous) subclass `this` 
            // will have the subclasses type and it can use 
            // invokeExact(), where this class could not 
            // (because we can't cast or even name the type 
            // of this)
            return $invoke$(handle);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}