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

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.wrapping.WrappedJavaIterator;
import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;
import org.eclipse.ceylon.compiler.java.wrapping.Wrappings;

import ceylon.language.Entry;
import ceylon.language.Iterator;
import ceylon.language.impl.BaseMap;

/**
 * A wrapper for a Java List that satisfies {@code ceylon.language.List}.
 */
class WrappedJavaMap<JavaKey,JavaItem,CeylonKey,CeylonItem> extends BaseMap<CeylonKey,CeylonItem> {

    private static final long serialVersionUID = 1L;
    private final java.util.Map<JavaKey,JavaItem> jMap;
    private final TypeDescriptor $reified$Key;
    private final TypeDescriptor $reified$Item;
    private Wrapping<JavaKey, CeylonKey> keyWrapping;
    private Wrapping<JavaItem, CeylonItem> itemWrapping;

    public WrappedJavaMap(TypeDescriptor $reified$Key, TypeDescriptor $reified$Item, 
            java.util.Map<JavaKey,JavaItem> jMap,
            Wrapping<JavaKey,CeylonKey> keyWrapping,
            Wrapping<JavaItem, CeylonItem> itemWrapping) {
        super($reified$Key, $reified$Item);
        this.$reified$Key = $reified$Key;
        this.$reified$Item = $reified$Item;
        this.jMap = jMap;
        this.keyWrapping = keyWrapping;
        this.itemWrapping = itemWrapping;
    }

    @Override
    public boolean defines(Object key) {
        return jMap.containsKey(key);
    }

    @Override
    public CeylonItem get(Object key) {
        return itemWrapping.wrap(jMap.get(keyWrapping.inverted().wrap((CeylonKey)key)));
    }

    @Override
    public Iterator<? extends Entry<? extends CeylonKey, ? extends CeylonItem>> iterator() {
        return new WrappedJavaIterator(null, jMap.entrySet(), 
                Wrappings.toCeylonEntry($reified$Key, $reified$Item, keyWrapping, itemWrapping));
    }
    
    public java.util.Map<JavaKey,JavaItem> unwrap() {
        return jMap;
    }

}