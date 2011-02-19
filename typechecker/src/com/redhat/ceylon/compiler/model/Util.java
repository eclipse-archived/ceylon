package com.redhat.ceylon.compiler.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {

    static Map<TypeParameter,ProducedType> arguments(Declaration d, List<ProducedType> typeArguments) {
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        if (d instanceof Generic) {
            Generic g = (Generic) d;
            for (int i=0; i<g.getTypeParameters().size(); i++) {
                map.put(g.getTypeParameters().get(i), typeArguments.get(i));
            }
        }
        return map;
    }

    public static boolean acceptsArguments(Declaration d, List<ProducedType> typeArguments) {
        if (typeArguments==null) {
            return false;
        }
        else {
            if (d instanceof Generic) {
                //TODO: assignability!!
                return ((Generic) d).getTypeParameters().size()==typeArguments.size();
            }
            else {
                return typeArguments.isEmpty();
            }
        }
    }

}
