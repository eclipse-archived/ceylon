package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Util {

    /**
     * Is the second scope contained by the first scope?
     */
    public static boolean contains(Scope outer, Scope inner) {
        while (inner!=null) {
            if (inner.equals(outer)) {
                return true;
            }
            inner = inner.getContainer();
        }
        return false;
    }
    
    /**
     * Get the class or interface that "this" and "super" 
     * refer to. 
     */
    public static ClassOrInterface getContainingClassOrInterface(Scope scope) {
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                return (ClassOrInterface) scope;
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Get the class or interface that "outer" refers to. 
     */
    public static ProducedType getOuterClassOrInterface(Scope scope) {
        Boolean foundInner = false;
        while (!(scope instanceof Package)) {
            if (scope instanceof ClassOrInterface) {
                if (foundInner) {
                    return ((ClassOrInterface) scope).getType();
                }
                else {
                    foundInner = true;
                }
            }
            scope = scope.getContainer();
        }
        return null;
    }
    
    /**
     * Convenience method to bind a single type argument 
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, ProducedType typeArgument) {
        return declaration.getProducedType(null, Collections.singletonList(typeArgument));
    }

    /**
     * Convenience method to bind a list of type arguments
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, ProducedType... typeArguments) {
        return declaration.getProducedType(null, Arrays.asList(typeArguments));
    }

    static boolean isResolvable(Declaration declaration) {
        return declaration.getName()!=null &&
            !(declaration instanceof Setter) && //return getters, not setters
            !(declaration instanceof Class && 
                    Character.isLowerCase(declaration.getName().charAt(0))); //don't return the type associated with an object dec 
    }
    
    static boolean isNamed(String name, Declaration d) {
        return d.getName()!=null && d.getName().equals(name);
    }
    
    static boolean isNameMatching(String startingWith, Declaration d) {
        return d.getName()!=null && 
            d.getName().toLowerCase().startsWith(startingWith.toLowerCase());
    }
    
    public static boolean erasureMatches(Declaration d, List<String> erasure) {
    	if (erasure!=null) {
    		if (d instanceof Functional) {
        		List<ParameterList> pls = ((Functional) d).getParameterLists();
        		if (pls==null || pls.isEmpty()) return false;
				List<Parameter> params = pls.get(0).getParameters();
        		if (params.size()!=erasure.size()) return false;
				for (int i=0; i<params.size(); i++) {
        			TypeDeclaration ptd = params.get(i).getTypeDeclaration();
					if (!ptd.getName().equals(erasure.get(i))) {
						return false;
					}
        		}
				return true;
    		}
    		else {
    			return false;
    		}
    	}
    	else {
    	    return true;
    	}
    }

    /**
     * Collect together type arguments given a list of 
     * type arguments to a declaration and the receiving 
     * type.
     * 
     * @return a map of type parameter to type argument
     *  
     * @param declaration a declaration
     * @param receivingType the receiving produced type 
     *        of which the declaration is a member
     * @param typeArguments explicit or inferred type 
     *        arguments of the declaration
     */
    static Map<TypeParameter,ProducedType> arguments(Declaration declaration, 
            ProducedType receivingType, List<ProducedType> typeArguments) {
        Map<TypeParameter, ProducedType> map = getArgumentsOfOuterType(receivingType);
        //now turn the type argument tuple into a
        //map from type parameter to argument
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

	public static Map<TypeParameter, ProducedType> getArgumentsOfOuterType(
			ProducedType receivingType) {
		Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        //make sure we collect all type arguments
        //from the whole qualified type!
        ProducedType dt = receivingType;
        while (dt!=null) {
            map.putAll(dt.getTypeArguments());
            dt = dt.getQualifyingType();
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
        ProducedType selfType = pt.getDeclaration().getSelfType();
		if (selfType!=null) {
        	pt = selfType.substitute(pt.getTypeArguments()); //canonicalize type with self type to the self type
        }
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType t: pt.getDeclaration().getCaseTypes() ) {
                addToUnion( list, t.substitute(pt.getTypeArguments()) );
            }
        }
        else {
            Boolean included = pt.isWellDefined();
            if (included) {
                for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                    ProducedType t = iter.next();
                    if (pt.isSubtypeOf(t)) {
                        included = false;
                        break;
                    }
                    //TODO: I think in some very rare occasions 
                    //      this can cause stack overflows!
                    else if (pt.isSupertypeOf(t)) {
                        iter.remove();
                    }
                }
            }
            if (included) {
                list.add(pt);
            }
        }
    }
    
    /**
     * Helper method for eliminating duplicate types from
     * lists of types that form an intersection type, taking 
     * into account that a supertype is a "duplicate" of its
     * subtype.
     */
    public static void addToIntersection(List<ProducedType> list, ProducedType pt, Unit unit) {
        if (pt==null) {
            return;
        }
        ProducedType selfType = pt.getDeclaration().getSelfType();
		if (selfType!=null) {
        	pt = selfType.substitute(pt.getTypeArguments()); //canonicalize type with self type to the self type
        }
        if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType t: pt.getDeclaration().getSatisfiedTypes() ) {
                addToIntersection(list, t.substitute(pt.getTypeArguments()), unit);
            }
        }
        else {
            Boolean included = pt.isWellDefined();
            if (included) {
                for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                    ProducedType t = iter.next();
                    if (pt.isSupertypeOf(t)) {
                        included = false;
                        break;
                    }
                    //TODO: I think in some very rare occasions 
                    //      this can cause stack overflows!
                    else if (pt.isSubtypeOf(t)) {
                        iter.remove();
                    }
                    else if ( pt.getDeclaration().equals(t.getDeclaration()) ) {
                        //canonicalize T<InX,OutX>&T<InY,OutY> to T<InX|InY,OutX&OutY>
                        TypeDeclaration td = pt.getDeclaration();
                        List<ProducedType> args = new ArrayList<ProducedType>();
                        for (int i=0; i<td.getTypeParameters().size(); i++) {
                            TypeParameter tp = td.getTypeParameters().get(i);
                            ProducedType pta = pt.getTypeArguments().get(tp);
                            ProducedType ta = t.getTypeArguments().get(tp);
                            if (tp.isInvariant()) {
                                if (!pta.isExactly(ta)) {
                                    //the meet of invariant types with different 
                                    //arguments is empty i.e. Bottom
                                    list.clear();
                                    list.add( new BottomType(unit).getType() );
                                    return;
                                }
                                else {
                                    args.add(pta);
                                }
                            }
                            if (tp.isContravariant()) {
                                List<ProducedType> ul = new ArrayList<ProducedType>();
                                addToUnion(ul, ta);
                                addToUnion(ul, pta);
                                UnionType ut = new UnionType(unit);
                                ut.setCaseTypes(ul);
                                args.add(ut.getType());
                            }
                            if (tp.isCovariant()) {
                                List<ProducedType> il = new ArrayList<ProducedType>();
                                addToIntersection(il, ta, unit);
                                addToIntersection(il, pta, unit);
                                IntersectionType it = new IntersectionType(unit);
                                it.setSatisfiedTypes(il);
                                args.add(it.canonicalize().getType());
                            }
                        }
                        iter.remove();
                        //TODO: broken handling of member types!
                        list.add( td.getProducedType(pt.getQualifyingType(), args) );
                        return;
                    }
                    else {
                        //Unit unit = pt.getDeclaration().getUnit();
                        TypeDeclaration nd = unit.getNothingDeclaration();
                        if (pt.getDeclaration() instanceof Class &&
                                t.getDeclaration() instanceof Class ||
                            pt.getDeclaration() instanceof Interface &&
                                t.getDeclaration().equals(nd) ||
                                //t.getDeclaration().getQualifiedNameString().equals("ceylon.language.Nothing") ||
                            t.getDeclaration() instanceof Interface &&
                                pt.getDeclaration().equals(nd)) {
                                //pt.getDeclaration().getQualifiedNameString().equals("ceylon.language.Nothing")) {
                            //the meet of two classes unrelated by inheritance, or
                            //of Nothing with an interface type is empty
                            list.clear();
                            list.add( new BottomType(unit).getType() );
                            return;
                        }
                    }
                }
            }
            if (included) {
                list.add(pt);
            }
        }
    }
    
    public static String formatPath(List<String> path) {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<path.size(); i++) {
            sb.append(path.get(i));
            if (i<path.size()-1) sb.append('.');
        }
        return sb.toString();
    }

    static boolean addToSupertypes(List<ProducedType> list, ProducedType st) {
        for (ProducedType et: list) {
            if (st.getDeclaration().equals(et.getDeclaration()) && //return both a type and its self type
            		st.isExactly(et)) {
                return false;
            }
        }
        list.add(st);
        return true;
    }

    public static ProducedType unionType(ProducedType lhst, ProducedType rhst, Unit unit) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        addToUnion(list, rhst);
        addToUnion(list, lhst);
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(list);
        return ut.getType();
    }

    public static ProducedType intersectionType(ProducedType lhst, ProducedType rhst, Unit unit) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        addToIntersection(list, rhst, unit);
        addToIntersection(list, lhst, unit);
        IntersectionType it = new IntersectionType(unit);
        it.setSatisfiedTypes(list);
        return it.canonicalize().getType();
    }

    public static boolean isElementOfUnion(UnionType ut, TypeDeclaration td) {
        for (TypeDeclaration ct: ut.getCaseTypeDeclarations()) {
            if (ct.equals(td)) return true;
        }
        return false;
    }
    
    public static boolean isElementOfIntersection(IntersectionType ut, TypeDeclaration td) {
        for (TypeDeclaration ct: ut.getSatisfiedTypeDeclarations()) {
            if (ct.equals(td)) return true;
        }
        return false;
    }
    
}
