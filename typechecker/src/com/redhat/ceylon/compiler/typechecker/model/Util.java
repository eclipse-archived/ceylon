package com.redhat.ceylon.compiler.typechecker.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Util {

    static Map<TypeParameter,ProducedType> arguments(Declaration d, ProducedType declaringType, List<ProducedType> typeArguments) {
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        if (declaringType!=null) {
            map.putAll(declaringType.getTypeArguments());
        }
        if (d instanceof Generic) {
            Generic g = (Generic) d;
            for (int i=0; i<g.getTypeParameters().size(); i++) {
                if (typeArguments.size()>i) {
                    map.put(g.getTypeParameters().get(i), typeArguments.get(i));
                }
            }
        }
        return map;
    }

}
