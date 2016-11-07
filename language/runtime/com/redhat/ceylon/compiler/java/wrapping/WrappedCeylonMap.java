package com.redhat.ceylon.compiler.java.wrapping;

import java.util.AbstractMap;
import java.util.Set;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * A wrapper for a Ceylon List that satisfies {@code java.util.List}
 */
class WrappedCeylonMap<CeylonKey,CeylonItem, JavaKey,JavaItem> extends AbstractMap<JavaKey,JavaItem> {

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