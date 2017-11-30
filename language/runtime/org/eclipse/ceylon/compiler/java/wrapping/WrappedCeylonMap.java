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

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Set;

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

import org.eclipse.ceylon.compiler.java.wrapping.WrappedCeylonSet;
import org.eclipse.ceylon.compiler.java.wrapping.Wrapping;
import org.eclipse.ceylon.compiler.java.wrapping.Wrappings;

/**
 * A wrapper for a Ceylon List that satisfies {@code java.util.List}
 */
class WrappedCeylonMap<CeylonKey,CeylonItem, JavaKey,JavaItem> 
        extends AbstractMap<JavaKey,JavaItem> 
        implements Serializable {

    private static final long serialVersionUID = 8123632437919187363L;
    
    private ceylon.language.Map<? extends CeylonKey,? extends CeylonItem> cMap;
    private WrappedCeylonSet<ceylon.language.Entry<CeylonKey, CeylonItem>, java.util.Map.Entry<JavaKey, JavaItem>> entrySet;

    public WrappedCeylonMap(TypeDescriptor $reified$Key, TypeDescriptor $reified$Item, 
            ceylon.language.Map<CeylonKey,CeylonItem> cMap,
            Wrapping<CeylonKey,JavaKey> keyWrapping,
            Wrapping<CeylonItem,JavaItem> itemWrapping) {
        super();
        this.cMap = cMap;
        Wrapping<ceylon.language.Entry<CeylonKey, CeylonItem>, java.util.Map.Entry<JavaKey, JavaItem>> inverted = Wrappings.toCeylonEntry($reified$Key, $reified$Item, keyWrapping.inverted(), itemWrapping.inverted()).inverted();
        this.entrySet = new WrappedCeylonSet<ceylon.language.Entry<CeylonKey, CeylonItem>, java.util.Map.Entry<JavaKey, JavaItem>>(
                (ceylon.language.Collection)cMap, 
                inverted);
    }

    @Override
    public Set<java.util.Map.Entry<JavaKey, JavaItem>> entrySet() {
        return entrySet;
    }
    
    public ceylon.language.Map<? extends CeylonKey,? extends CeylonItem> unwrap() {
        return cMap;
    }
}