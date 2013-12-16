package com.redhat.ceylon.compiler.typechecker.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;

public class ProducedTypeCache {

    // need a special value for null because ConcurrentHashMap does not support null
    private final static ProducedType NULL_VALUE = new UnknownType(null).getType();
    
    // need ConcurrentHashMap even for the cache, otherwise get/put/containsKey can get info infinite loops
    // on concurrent operations
    private final Map<ProducedType, Map<TypeDeclaration, ProducedType>> superTypes = new ConcurrentHashMap<>();
    
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
        ProducedType ret = cache.get(dec);
        return ret == NULL_VALUE ? null : ret;
    }

    public void put(ProducedType producedType, TypeDeclaration dec, ProducedType superType) {
        Map<TypeDeclaration, ProducedType> cache = superTypes.get(producedType);
        if(cache == null){
            // need ConcurrentHashMap even for the cache, otherwise get/put/containsKey can get info infinite loops
            // on concurrent operations
            cache = new ConcurrentHashMap<>();
            superTypes.put(producedType, cache);
        }
        if(superType == null)
            superType = NULL_VALUE;
        cache.put(dec, superType);
    }

    public void clear(){
        superTypes.clear();
    }

    public void clearForDeclaration(TypeDeclaration decl) {
        // clear the whole cache for now, unless I can figure out exactly what to
        // clear
        clear();
    }
}
