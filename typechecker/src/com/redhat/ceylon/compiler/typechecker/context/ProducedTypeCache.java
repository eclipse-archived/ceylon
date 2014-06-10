package com.redhat.ceylon.compiler.typechecker.context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.redhat.ceylon.compiler.typechecker.model.ProducedType;
import com.redhat.ceylon.compiler.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.compiler.typechecker.model.UnknownType;

public class ProducedTypeCache {

    public interface CacheEnabler {
        boolean isCacheEnabled();
    }
    
    // need a special value for null because ConcurrentHashMap does not support null
    private final static ProducedType NULL_VALUE = new UnknownType(null).getType();
    private static CacheEnabler cacheEnabler = new CacheEnabler() {
        @Override
        public boolean isCacheEnabled() {
            return false;
        }
    };
    
    public static void setCacheEnabler(CacheEnabler newCacheEnabler) {
        if (newCacheEnabler != null) {
            cacheEnabler = newCacheEnabler;
        }
    }
    
    public static boolean isCachingEnabled() {
        return cacheEnabler.isCacheEnabled();
    }
    
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
    
    public void clearNullValues() {
        List<ProducedType> cachesToremove = new LinkedList<>();
        for (Map.Entry<ProducedType, Map<TypeDeclaration, ProducedType>> entry : superTypes.entrySet()) {
            Map<TypeDeclaration, ProducedType> cache = entry.getValue();
            if (cache == null) {
                cachesToremove.add(entry.getKey());
            } else {
                List<TypeDeclaration> valuesToremove = new LinkedList<>();
                for (Map.Entry<TypeDeclaration, ProducedType> cacheEntry : cache.entrySet()) {
                    if (cacheEntry.getValue() == NULL_VALUE) {
                        valuesToremove.add(cacheEntry.getKey());
                    }
                }
                for (TypeDeclaration toRemove : valuesToremove) {
                    cache.remove(toRemove);
                }
            }
        }
        for (ProducedType toRemove : cachesToremove) {
            superTypes.remove(toRemove);
        }
    }
}
