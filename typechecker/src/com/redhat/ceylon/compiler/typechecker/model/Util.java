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

    public static boolean isResolvable(Declaration declaration) {
        return declaration.getName()!=null &&
            !(declaration instanceof Setter) && //return getters, not setters
            !(declaration instanceof ValueParameter && 
                    ((ValueParameter) declaration).isHidden()) &&
            !declaration.isAnonymous(); //don't return the type associated with an object dec 
    }
    
    static boolean isParameter(Declaration d) {
        return d instanceof Parameter
                || d instanceof TypeParameter;
    }

    public static boolean isAbstraction(Declaration d) {
        return d instanceof Functional && 
                ((Functional) d).isAbstraction();
    }

    static boolean notOverloaded(Declaration d) {
        return !(d instanceof Functional) || 
                !((Functional) d).isOverloaded() ||
                ((Functional) d).isAbstraction();
    }
    
    static boolean hasMatchingSignature(List<ProducedType> signature, Declaration d) {
        if (d instanceof Class && ((Class) d).isAbstract()) {
            return false;
        }
        if (d instanceof Functional) {
            Functional f = (Functional) d;
            if (f.isAbstraction()) {
                return false;
            }
            else {
                List<ParameterList> pls = f.getParameterLists();
                if (pls!=null && !pls.isEmpty()) {
                    List<Parameter> params = pls.get(0).getParameters();
                    if (signature.size()!=params.size()) {
                        return false;
                    }
                    else {
                        for (int i=0; i<params.size(); i++) {
                            //ignore optionality for resolving overloads, since
                            //all all Java method params are treated as optional
                            ProducedType pdt = params.get(i).getType();
                            ProducedType sdt = signature.get(i);
                            if (pdt==null || sdt==null) return false;
                            ProducedType paramType = d.getUnit().getDefiniteType(pdt);
                            ProducedType sigType = d.getUnit().getDefiniteType(sdt);
                            TypeDeclaration paramTypeDec = paramType.getDeclaration();
                            TypeDeclaration sigTypeDec = sigType.getDeclaration();
                            if (sigTypeDec==null || paramTypeDec==null) return false;
                            if (sigTypeDec instanceof UnknownType || paramTypeDec instanceof UnknownType) return false;
                            if (!erase(sigTypeDec).inherits(erase(paramTypeDec)) &&
                                    underlyingTypesUnequal(paramType, sigType)) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                else {
                    return false;
                }
            }
        }
        else {
            return false;
        }
    }

    private static boolean underlyingTypesUnequal(ProducedType paramType,
            ProducedType sigType) {
        return sigType.getUnderlyingType()==null || 
                paramType.getUnderlyingType()==null || 
                !sigType.getUnderlyingType().equals(paramType.getUnderlyingType());
    }
    
    static boolean betterMatch(Declaration d, Declaration r) {
        if (d instanceof Functional && r instanceof Functional) {
            List<ParameterList> dpls = ((Functional) d).getParameterLists();
            List<ParameterList> rpls = ((Functional) r).getParameterLists();
            if (dpls!=null&&!dpls.isEmpty() && rpls!=null&&!rpls.isEmpty()) {
                List<Parameter> dpl = dpls.get(0).getParameters();
                List<Parameter> rpl = rpls.get(0).getParameters();
                if (dpl.size()==rpl.size()) {
                    for (int i=0; i<dpl.size(); i++) {
                        ProducedType paramType = d.getUnit().getDefiniteType(dpl.get(i).getType());
                        TypeDeclaration paramTypeDec = paramType.getDeclaration();
                        ProducedType otherType = d.getUnit().getDefiniteType(rpl.get(i).getType());
                        TypeDeclaration otherTypeDec = otherType.getDeclaration();
                        if (otherTypeDec==null || paramTypeDec==null) return false;
                        if (otherTypeDec instanceof UnknownType || paramTypeDec instanceof UnknownType) return false;
                        if (!erase(paramTypeDec).inherits(erase(otherTypeDec)) &&
                                underlyingTypesUnequal(paramType, otherType)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isNamed(String name, Declaration d) {
        String dname = d.getName();
        return dname!=null && dname.equals(name);
    }
    
    private static TypeDeclaration erase(TypeDeclaration paramType) {
        if (paramType instanceof TypeParameter) {
            if ( paramType.getSatisfiedTypes().isEmpty() ) {
                return paramType.getExtendedTypeDeclaration();
            }
            else {
                //TODO: is this actually correct? What is Java's
                //      rule here?
                return paramType.getSatisfiedTypeDeclarations().get(0);
            }
        }
        else if (paramType instanceof UnionType || 
                paramType instanceof IntersectionType) {
            //TODO: this is pretty sucky, cos in theory a
            //      union or intersection might be assignable
            //      to the parameter type with a typecast
            return paramType.getUnit().getObjectDeclaration();
        }
        else {
            return paramType;
        }
    }
    
    static boolean isNameMatching(String startingWith, Declaration d) {
        return d.getName()!=null && 
            d.getName().toLowerCase().startsWith(startingWith.toLowerCase());
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
            //implement the rule that Foo&Bar==Bottom if 
            //there exists some Baz of Foo | Bar
            //i.e. the intersection of disjoint types is
            //empty
            for (ProducedType supertype: pt.getSupertypes()) {
                List<TypeDeclaration> ctds = supertype.getDeclaration().getCaseTypeDeclarations();
                if (ctds!=null) {
                    TypeDeclaration ctd=null;
                    for (TypeDeclaration ct: ctds) {
                        if (pt.getSupertype(ct)!=null) {
                            ctd = ct;
                            break;
                        }
                    }
                    if (ctd!=null) {
                        for (TypeDeclaration ct: ctds) {
                            if (ct!=ctd) {
                                for (ProducedType t: list) {
                                    if (t.getSupertype(ct)!=null) {
                                        list.clear();
                                        list.add( new BottomType(unit).getType() );
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
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
                    else if (pt.getDeclaration() instanceof ClassOrInterface && 
                            t.getDeclaration() instanceof ClassOrInterface && 
                            pt.getDeclaration().equals(t.getDeclaration()) ) {
                        //canonicalize T<InX,OutX>&T<InY,OutY> to T<InX|InY,OutX&OutY>
                        TypeDeclaration td = pt.getDeclaration();
                        List<ProducedType> args = new ArrayList<ProducedType>();
                        for (int i=0; i<td.getTypeParameters().size(); i++) {
                            TypeParameter tp = td.getTypeParameters().get(i);
                            ProducedType pta = pt.getTypeArguments().get(tp);
                            ProducedType ta = t.getTypeArguments().get(tp);
                            if (tp.isContravariant()) {
                                args.add(unionType(pta, ta, unit));
                            }
                            else if (tp.isCovariant()) {
                                args.add(intersectionType(pta, ta, unit));
                            }
                            else {
                                TypeDeclaration ptad = pta.getDeclaration();
                                TypeDeclaration tad = ta.getDeclaration();
                                if ( !(ptad instanceof TypeParameter) &&
                                     !(tad instanceof TypeParameter) &&
                                        !ptad.equals(tad) ) {
                                    //if the two type arguments have provably 
                                    //different types, then the meet of the
                                    //two intersected invariant types is empty
                                    //TODO: this is too weak, we should really
                                    //      recursively search for type parameter
                                    //      arguments and if we don't find any
                                    //      we can reduce to Bottom
                                    list.clear();
                                    args.add( new BottomType(unit).getType() );
                                    return;
                                }
                                else {
                                    //TODO: this is not correct: the intersection
                                    //      of two different instantiations of an
                                    //      invariant type is actually Bottom
                                    //      unless the type arguments are equivalent
                                    //      or are type parameters that might 
                                    //      represent equivalent types at runtime.
                                    //      Therefore, a method T x(T y) of Inv<T> 
                                    //      should have the signature:
                                    //             Foo&Bar x(Foo|Bar y)
                                    //      on the intersection Inv<Foo>&Inv<Bar>.
                                    //      But this code gives it the more 
                                    //      restrictive signature:
                                    //             Foo&Bar x(Foo&Bar y)
                                    args.add(intersectionType(pta, ta, unit));
                                }
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
                                t.getDeclaration() instanceof Class &&
                                t.getDeclaration().equals(nd) ||
                                //t.getDeclaration().getQualifiedNameString().equals("ceylon.language.Nothing") ||
                            t.getDeclaration() instanceof Interface &&
                            pt.getDeclaration() instanceof Class &&
                                pt.getDeclaration().equals(nd)) {
                                //pt.getDeclaration().getQualifiedNameString().equals("ceylon.language.Nothing")) {
                            //the meet of two classes unrelated by inheritance, or
                            //of Nothing with an interface type is empty
                            list.clear();
                            list.add( unit.getBottomDeclaration().getType() );
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

    public static boolean isElementOfUnion(UnionType ut, ClassOrInterface ci) {
        for (TypeDeclaration ct: ut.getCaseTypeDeclarations()) {
            if (ct instanceof ClassOrInterface && ct.equals(ci)) {
                return true;
            }
        }
        return false;
    }
    
    /*public static boolean isElementOfIntersection(IntersectionType ut, ClassOrInterface td) {
        for (TypeDeclaration ct: ut.getSatisfiedTypeDeclarations()) {
            if (ct instanceof ClassOrInterface && ct.equals(td)) {
                return true;
            }
        }
        return false;
    }*/
    
    public static Declaration lookupMember(List<Declaration> members, String name,
            List<ProducedType> signature, boolean includeParameters) {
        List<Declaration> results = new ArrayList<Declaration>();
        Declaration inexactMatch = null;
        for (Declaration d: members) {
            if (isResolvable(d) && isNamed(name, d) &&
                    (includeParameters || !isParameter(d))) {
                if (signature==null) {
                    //no argument types: either a type 
                    //declaration, an attribute, or a method 
                    //reference - don't return overloaded
                    //forms of the declaration (instead
                    //return the "abstraction" of them)
                    if (notOverloaded(d)) {
                        //by returning the first thing we
                        //find, we implement the rule that
                        //parameters hide attributes with
                        //the same name in the body of a
                        //class (a bit of a hack solution)
                        return d;
                    }
                }
                else {
                    if (notOverloaded(d)) {
                        //we have found either a non-overloaded
                        //declaration, or the "abstraction" 
                        //which of all the overloaded forms 
                        //of the declaration
                        inexactMatch = d;
                    }
                    if (hasMatchingSignature(signature, d)) {
                        //we have found an exactly matching 
                        //overloaded declaration
                        boolean add=true;
                        for (Iterator<Declaration> i = results.iterator(); i.hasNext();) {
                            Declaration o = i.next();
                            if (betterMatch(d, o)) {
                                i.remove();
                            }
                            else if (betterMatch(o, d)) { //TODO: note assymmetry here resulting in nondeterminate behavior!
                                add=false;
                            }
                        }
                        if (add) results.add(d);
                    }
                }
            }
        }
        switch (results.size()) {
        case 0:
            //no exact match, so return the non-overloaded
            //declaration or the "abstraction" of the 
            //overloaded declaration
            return inexactMatch;
        case 1:
            //exactly one exact match, so return it
            return results.get(0);
        default:
            //more than one matching overloaded declaration,
            //so return the "abstraction" of the overloaded
            //declaration
            return inexactMatch;
        }
    }

}
