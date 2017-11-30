/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.wrapping;

import java.util.NoSuchElementException;

import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;

import ceylon.language.Finished;

/**
 * A wrapper for ceylon Iterator that satisfies {@code java.util.Iterator}
 */
class WrappedCeylonIterator<Out, In> implements java.util.Iterator<Out> {
    
    private final ceylon.language.Iterator<? extends In> cIterator;
    private Object next;
    private final Wrapping<In, Out> elementWrapping;
    
    public WrappedCeylonIterator(ceylon.language.Iterable<In,?> cSet, Wrapping<In, Out> elementWrapping) {
        cIterator = cSet.iterator();
        next = cIterator.next();
        this.elementWrapping = elementWrapping;
    }
    

    @Override
    public boolean hasNext() {
        return !(next instanceof Finished);
    }

    @Override
    public Out next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        Object next = this.next;
        this.next = cIterator.next();
        return (Out)elementWrapping.wrap((In)next);
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}