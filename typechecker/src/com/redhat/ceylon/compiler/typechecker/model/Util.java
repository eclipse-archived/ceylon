package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Util {

    static boolean isResolvable(Declaration declaration) {
        return declaration.getName()!=null &&
            !(declaration instanceof Setter) && //return getters, not setters
            !(declaration instanceof Class && 
                    Character.isLowerCase(declaration.getName().charAt(0))); //don't return the type associated with an object dec 
    }
    
    static boolean isNamed(String name, Declaration d) {
        return d.getName()!=null && d.getName().equals(name);
    }
    
    /**
     * Produce a map of type parameter to type argument
     * given a list of type arguments to a declaration 
     * and the containing type.
     *  
     * @param declaration a declaration
     * @param declaringType the produced type of which
     *        the declaration is a member
     * @param typeArguments explicit or inferred type 
     *        arguments of the declaration
     */
    static Map<TypeParameter,ProducedType> arguments(Declaration declaration, ProducedType declaringType, List<ProducedType> typeArguments) {
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        if (declaringType!=null) {
            map.putAll(declaringType.getTypeArguments());
        }
        if (declaration instanceof Generic) {
            Generic g = (Generic) declaration;
            for (int i=0; i<g.getTypeParameters().size(); i++) {
                if (typeArguments.size()>i) {
                    map.put(g.getTypeParameters().get(i), typeArguments.get(i));
                }
            }
        }
        return map;
    }
    
    static <T> List<T> list(List<T> list, T element) {
        List<T> result = new ArrayList<T>();
        result.addAll(list);
        result.add(element);
        return result;
    }

    /**
     * Helper method for eliminating duplicate types from
     * lists of types that form a union type, taking into
     * account that a subtype is a "duplicate" of its
     * supertype.
     */
    public static void addToUnion(List<ProducedType> list, ProducedType pt) {
        if (pt==null) {
            return;
        }
        if (pt.getDeclaration() instanceof UnionType) {
            for (Iterator<ProducedType> iter = pt.getDeclaration().getCaseTypes().iterator(); iter.hasNext();) {
                ProducedType t = iter.next();
                addToUnion( list, t.substitute(pt.getTypeArguments()) );
            }
        }
        else {
            Boolean included = false;
            for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                ProducedType t = iter.next();
                if (pt.isSubtypeOf(t)) {
                    included = true;
                    break;
                }
                //TODO: I think in some very rare occasions 
                //      this can cause stack overflows!
                else if (pt.isSupertypeOf(t)) {
                    iter.remove();
                }
            }
            if (!included) {
                list.add(pt);
            }
        }
    }

}
