package com.redhat.ceylon.compiler.typechecker.context;

import java.util.HashMap;
import java.util.Map;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;

public class ProducedTypeCache {

    private final Map<ProducedType, Map<TypeDeclaration, ProducedType>> superTypes = new HashMap<>();
    
    public boolean containsKey(ProducedType producedType, TypeDeclaration dec) {
        Map<TypeDeclaration, ProducedType> cache = superTypes.get(producedType);
        if(cache == null)
            return false;
        return cache.containsKey(dec);
    }

    public ProducedType get(ProducedType producedType, TypeDeclaration dec) {
        Map<TypeDeclaration, ProducedType> cache = superTypes.get(producedType);
        if(cache == null)
            return null;
        return cache.get(dec);
    }

    public void put(ProducedType producedType, TypeDeclaration dec, ProducedType superType) {
        Map<TypeDeclaration, ProducedType> cache = superTypes.get(producedType);
        if(cache == null){
            cache = new HashMap<>();
            superTypes.put(producedType, cache);
        }
        cache.put(dec, superType);
    }

}
