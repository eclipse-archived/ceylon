package com.redhat.ceylon.model.typechecker.context;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.redhat.ceylon.model.typechecker.model.Type;
import com.redhat.ceylon.model.typechecker.model.TypeDeclaration;
import com.redhat.ceylon.model.typechecker.model.UnknownType;

public class TypeCache {
    
    private static boolean cachingEnabledByDefault = false;
    
    public static void setEnabledByDefault(boolean enabled) {
        cachingEnabledByDefault = enabled;
    }
    
    private static final ThreadLocal<Boolean> cachingEnabled = 
            new ThreadLocal<Boolean>();
    
    public static Boolean setEnabled(Boolean enabled) {
        Boolean was = cachingEnabled.get();
        cachingEnabled.set(enabled);
        return was;
    }
    
    public static boolean isEnabled() {
        Boolean cie = cachingEnabled.get();
        return cie == null ? cachingEnabledByDefault : cie;
    }
    
    // need a special value for null because ConcurrentHashMap does not support null
    private final static Type NULL_VALUE = new UnknownType(null).getType();
    // need ConcurrentHashMap even for the cache, otherwise get/put/containsKey can get info infinite loops
    // on concurrent operations
    private final Map<Type, Map<TypeDeclaration, Type>> superTypes = 
            new ConcurrentHashMap<Type, Map<TypeDeclaration, Type>>();
    
    public boolean containsKey(Type producedType, TypeDeclaration dec) {
        Map<TypeDeclaration, Type> cache = superTypes.get(producedType);
        if (cache == null) {
            return false;
        }
        return cache.containsKey(dec);
    }

    public Type get(Type producedType, TypeDeclaration dec) {
        Map<TypeDeclaration, Type> cache = superTypes.get(producedType);
        if (cache == null) {
            return null;
        }
        Type ret = cache.get(dec);
        return ret == NULL_VALUE ? null : ret;
    }

    public void put(Type producedType, TypeDeclaration dec, Type superType) {
        Map<TypeDeclaration, Type> cache = superTypes.get(producedType);
        if (cache == null) {
            // need ConcurrentHashMap even for the cache, otherwise get/put/containsKey can get info infinite loops
            // on concurrent operations
            cache = new ConcurrentHashMap<TypeDeclaration, Type>();
            superTypes.put(producedType, cache);
        }
        if (superType == null) {
            superType = NULL_VALUE;
        }
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
        List<Type> cachesToremove = new LinkedList<Type>();
        for (Map.Entry<Type, Map<TypeDeclaration, Type>> entry: 
                superTypes.entrySet()) {
            Map<TypeDeclaration, Type> cache = entry.getValue();
            if (cache == null) {
                cachesToremove.add(entry.getKey());
            }
            else {
                List<TypeDeclaration> valuesToremove = 
                        new LinkedList<TypeDeclaration>();
                for (Map.Entry<TypeDeclaration, Type> cacheEntry: 
                        cache.entrySet()) {
                    if (cacheEntry.getValue() == NULL_VALUE) {
                        valuesToremove.add(cacheEntry.getKey());
                    }
                }
                for (TypeDeclaration toRemove: valuesToremove) {
                    cache.remove(toRemove);
                }
            }
        }
        for (Type toRemove: cachesToremove) {
            superTypes.remove(toRemove);
        }
    }
}
