/********************************************************************************
 * Copyright (c) {date} Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.compiler.java.wrapping;

import java.io.Serializable;
import java.util.AbstractSet;

import org.eclipse.ceylon.compiler.java.Util;

import org.eclipse.ceylon.compiler.java.wrapping.WrappedCeylonIterator;
import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;

/**
 * A wrapper for a Ceylon List that satisfies {@code java.util.List} 
 */
class WrappedCeylonSet<In,Out> 
        extends AbstractSet<Out> 
        implements Serializable {

    private static final long serialVersionUID = 1800969777619067034L;
    
    // Collection because a c.l.Map is a Collection<Entry>
    // and we need to compute a j.u.Set as the entrySet()
    // of  a j.u.Map
    private final ceylon.language.Collection<In> cSet;
    private final Wrapping<In, Out> elementWrapping;

    public WrappedCeylonSet(ceylon.language.Collection<In> cSet, Wrapping<In, Out> elementWrapping) {
        this.cSet= cSet;
        this.elementWrapping = elementWrapping;
    }

    @Override
    public java.util.Iterator<Out> iterator() {
        return new WrappedCeylonIterator<Out, In>(cSet, elementWrapping);
    }

    @Override
    public int size() {
        return Util.toInt(cSet.getSize());
    }
    
    @Override
    public boolean contains(Object element) {
        Wrapping<Out, In> rev = elementWrapping.inverted();
        if (rev != null) {
            return cSet.contains(rev.wrap((Out)element));
        } else {
            return super.contains(element);
        }
    }
    
    ceylon.language.Collection<In> unwrap() {
        return cSet;
    }
}