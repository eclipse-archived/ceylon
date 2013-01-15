package com.redhat.ceylon.compiler.typechecker.model;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
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
            inner = inner.getScope();
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
    public static ProducedType producedType(TypeDeclaration declaration, 
    		ProducedType typeArgument) {
        return declaration.getProducedType(null, singletonList(typeArgument));
    }

    /**
     * Convenience method to bind a list of type arguments
     * to a toplevel type declaration.  
     */
    public static ProducedType producedType(TypeDeclaration declaration, 
    		ProducedType... typeArguments) {
        return declaration.getProducedType(null, asList(typeArguments));
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
    
    public static boolean isOverloadedVersion(Declaration decl) {
        if (decl instanceof Functional) {
            return ((Functional)decl).isOverloaded();
        }
        return false;
    }

    static boolean hasMatchingSignature(List<ProducedType> signature, 
    		boolean ellipsis, Declaration d) {
        return hasMatchingSignature(signature, ellipsis, d, true);
    }
    
    static boolean hasMatchingSignature(List<ProducedType> signature, 
    		boolean ellipsis, Declaration d, boolean excludeAbstractClasses) {
        if (excludeAbstractClasses && 
        		d instanceof Class && ((Class) d).isAbstract()) {
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
                    int size = params.size();
                    boolean hasSeqParam = pls.get(0).hasSequencedParameter();
					if (hasSeqParam) {
                        size--;
                        if (signature.size()<size) {
                            return false;
                        }
                    }
                    else if (signature.size()!=size) {
                        return false;
                    }
                    for (int i=0; i<size; i++) {
                        //ignore optionality for resolving overloads, since
                        //all Java method params are treated as optional
                        ProducedType pdt = params.get(i).getType();
                        if (pdt==null) return false;
                        ProducedType sdt = signature.get(i);
                        if (!matches(pdt, sdt, d)) return false;
                    }
                    if (hasSeqParam) {
                        ProducedType pdt = params.get(size).getType();
                        if (pdt==null) return false;
                        if (ellipsis){
                            // we must have exactly one spread param
                            if (signature.size() != size+1) return false;
                            ProducedType sdt = signature.get(size);
                            ProducedType isdt = d.getUnit().getIteratedType(sdt);
                            ProducedType ipdt = d.getUnit().getIteratedType(pdt);
                            if (!matches(ipdt, isdt, d)) return false;
                        }
                        else {
                            pdt = d.getUnit().getIteratedType(pdt);
                            for (int j=size; j<signature.size(); j++) {
                                ProducedType sdt = signature.get(j);
                                if (!matches(pdt, sdt, d)) return false;
                            }
                        }
                    }
                    else if (ellipsis) {
                        // if the method doesn't take sequenced params and we have an ellipsis
                        // let's not use it since we expect a variadic method
                        return false;
                    }
                    return true;
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
    
    private static boolean matches(ProducedType pdt, ProducedType sdt, Declaration d) {
        if (pdt==null || sdt==null) return false;
        ProducedType paramType = d.getUnit().getDefiniteType(pdt);
        ProducedType sigType = d.getUnit().getDefiniteType(sdt);
        ProducedType ast = sigType.getSupertype(d.getUnit().getArrayDeclaration());
        if (ast!=null) sigType = ast;
        if (sigType.isSubtypeOf(d.getUnit().getNullDeclaration().getType())) {
            return true;
        }
        if (isTypeUnknown(sigType) || isTypeUnknown(paramType)) return false;
        if (!erase(sigType.getDeclaration())
                .inherits(erase(paramType.getDeclaration())) &&
                underlyingTypesUnequal(paramType, sigType)) {
            return false;
        }
        return true;
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
                int dplSize = dpl.size();
                int rplSize = rpl.size();
                //ignore sequenced parameters
                boolean dhsp = dpls.get(0).hasSequencedParameter();
                boolean rhsp = rpls.get(0).hasSequencedParameter();
                //always prefer a signature without varargs 
                //over one with a varargs parameter
                if (!dhsp && rhsp) {
                    return true;
                }
                if (dhsp && !rhsp) {
                    return false;
                }
                //ignore sequenced parameters
                if (dhsp) { dplSize--; }
                if (rhsp) { rplSize--; }
                if (dplSize==rplSize) {
                    //if all parameters are of more specific
                    //or equal type, prefer it
                    for (int i=0; i<dplSize; i++) {
                        ProducedType paramType = d.getUnit().getDefiniteType(dpl.get(i).getType());
                        ProducedType otherType = d.getUnit().getDefiniteType(rpl.get(i).getType());
                        if (isTypeUnknown(otherType) || isTypeUnknown(paramType)) return false;
                        if (!erase(paramType.getDeclaration())
                                    .inherits(erase(otherType.getDeclaration())) &&
                                underlyingTypesUnequal(paramType, otherType)) {
                            return false;
                        }
                    }
                    // check sequenced parameters last
                    if (dhsp && rhsp){
                        ProducedType paramType = d.getUnit().getDefiniteType(dpl.get(dplSize).getType());
                        ProducedType otherType = d.getUnit().getDefiniteType(rpl.get(dplSize).getType());
                        if (isTypeUnknown(otherType) || isTypeUnknown(paramType)) return false;
                        paramType = d.getUnit().getIteratedType(paramType);
                        otherType = d.getUnit().getIteratedType(otherType);
                        if (isTypeUnknown(otherType) || isTypeUnknown(paramType)) return false;
                        if (!erase(paramType.getDeclaration())
                                .inherits(erase(otherType.getDeclaration())) &&
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
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType t: pt.getDeclaration().getCaseTypes() ) {
                addToUnion( list, t.substitute(pt.getTypeArguments()) );
            }
        }
        else if (pt.isWellDefined()) {
            boolean add=true;
            for (Iterator<ProducedType> iter = list.iterator(); iter.hasNext();) {
                ProducedType t = iter.next();
                if (pt.isSubtypeOf(t)) {
                    add=false;
                    break;
                }
                else if (pt.isSupertypeOf(t)) {
                    iter.remove();
                }
            }
            if (add) {
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
    public static void addToIntersection(List<ProducedType> list, ProducedType pt, 
    		Unit unit) {
        if (pt==null) {
            return;
        }
        if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType t: pt.getDeclaration().getSatisfiedTypes() ) {
                addToIntersection(list, t.substitute(pt.getTypeArguments()), unit);
            }
        }
        else {
            //implement the rule that Foo&Bar==Nothing if 
            //there exists some enumerated type Baz with
        	//    Baz of Foo | Bar 
        	//(the intersection of disjoint types is empty)
            for (ProducedType supertype: pt.getSupertypes()) {
                List<TypeDeclaration> ctds = supertype.getDeclaration()
                        .getCaseTypeDeclarations();
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
                                        list.add( new NothingType(unit).getType() );
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
                    else if (pt.isSubtypeOf(t)) {
                        iter.remove();
                    }
                    else if (haveUninhabitableIntersection(pt,t, unit)) {
                        list.clear();
                        list.add(unit.getNothingDeclaration().getType());
                        return;
                    }
                    else if (pt.getDeclaration() instanceof ClassOrInterface && 
                            t.getDeclaration() instanceof ClassOrInterface && 
                            pt.getDeclaration().equals(t.getDeclaration()) ) {
                        //canonicalize T<InX,OutX>&T<InY,OutY> to T<InX|InY,OutX&OutY>
                        iter.remove();
                        list.add(principalInstantiation(pt, t, unit));
                        return;
                    }
                    else if (emptyMeet(pt, t, unit)) {
                    	list.clear();
                    	list.add( unit.getNothingDeclaration().getType() );
                    	return;                            
                    }
                }
            }
            if (included) {
                list.add(pt);
            }
        }
    }

	/**
	 * The meet of two classes unrelated by inheritance,
	 * or of Null with an interface type is empty. The meet
	 * of an anonymous class with a type to which it is not
	 * assignable is empty.
	 */
	private static boolean emptyMeet(ProducedType pt, ProducedType t, Unit unit) {
		TypeDeclaration nd = unit.getNullDeclaration(); //TODO what about the anonymous type of null?
		TypeDeclaration ptd = pt.getDeclaration();
		TypeDeclaration td = t.getDeclaration();
		return ((ptd instanceof Class && td instanceof Class ||
		        ptd instanceof Interface && td instanceof Class &&
		        		td.equals(nd) ||
		        td instanceof Interface && ptd instanceof Class &&
		        		ptd.equals(nd)) &&
		    t.getSupertype(ptd)==null &&
		    pt.getSupertype(td)==null) ||
		    //the following test could be isFinal() 
		    //instead of isAnonymous()
		        ptd.isAnonymous() && !pt.isSubtypeOf(t) ||
		        td.isAnonymous() && !t.isSubtypeOf(pt);
	}

	/**
	 * Given two produced types for the exact same type
	 * constructor, return the principal instantiation
	 * of that type constructor for the intersection of 
	 * the two types.
	 * @param pt an instantiation of the type constructor
	 * @param t another instantiation of the type constructor
	 */
	private static ProducedType principalInstantiation(ProducedType pt,
			ProducedType t, Unit unit) {
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
		    	//TODO: this is not correct: the intersection
		    	//      of two different instantiations of an
		    	//      invariant type is actually Nothing
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
		return td.getProducedType(principalQualifyingType(pt, t, td, unit), args);
	}
	
	/**
	 * Given two instantiations of a qualified type constructor, 
	 * determine the qualifying type of the principal 
	 * instantiation of that type constructor for the 
	 * intersection of the two types.
	 */
	static ProducedType principalQualifyingType(ProducedType pt,
			ProducedType t, TypeDeclaration td, Unit unit) {
		ProducedType ptqt = pt.getQualifyingType();
		ProducedType tqt = t.getQualifyingType();
		ProducedType qt = null;
		if (ptqt!=null && tqt!=null && 
				td.getContainer() instanceof TypeDeclaration) {
			TypeDeclaration qtd = (TypeDeclaration) td.getContainer();
			ProducedType pst = ptqt.getSupertype(qtd);
			ProducedType st = tqt.getSupertype(qtd);
			if (pst!=null && st!=null) {
				qt = principalInstantiation(pst, st, unit);
			}
		}
		return qt;
	}
    
    /**
     * Determine if a type of form X<P>&X<Q> is equivalent to
     * Nothing where X<T> is invariant in T.
     * @param p the argument type P
     * @param q the argument type Q
     */
    private static boolean haveUninhabitableIntersection
            (ProducedType p, ProducedType q, Unit unit) {
    	List<TypeDeclaration> stds = p.getDeclaration().getSuperTypeDeclarations();
    	stds.retainAll(q.getDeclaration().getSuperTypeDeclarations());
    	for (TypeDeclaration std: stds) {
    		ProducedType pst = p.getSupertype(std);
    		ProducedType qst = q.getSupertype(std);
    		for (TypeParameter tp: std.getTypeParameters()) {
    			if (tp.isInvariant()) {
    				ProducedType psta = pst.getTypeArguments().get(tp);
    				ProducedType qsta = qst.getTypeArguments().get(tp);
    				if (psta!=null && psta.isWellDefined() &&
    						qsta!=null && psta.isWellDefined() &&
    						//what about types with UnknownType as an arg?
    						!pst.containsTypeParameters() && 
    						!qst.containsTypeParameters() &&
    						!pst.isExactly(qst)) {
    					return true;
    				}
    			}
    		}
    	}
		return false;
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
            		st.isExactlyInternal(et)) {
                return false;
            }
        }
        list.add(st);
        return true;
    }

    public static ProducedType unionType(ProducedType lhst, ProducedType rhst, 
    		Unit unit) {
        List<ProducedType> list = new ArrayList<ProducedType>();
        addToUnion(list, rhst);
        addToUnion(list, lhst);
        UnionType ut = new UnionType(unit);
        ut.setCaseTypes(list);
        return ut.getType();
    }

    public static ProducedType intersectionType(ProducedType lhst, ProducedType rhst, 
    		Unit unit) {
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
    
    public static Declaration lookupMember(List<Declaration> members, String name,
            List<ProducedType> signature, boolean ellipsis) {
        List<Declaration> results = new ArrayList<Declaration>();
        Declaration inexactMatch = null;
        for (Declaration d: members) {
            if (isResolvable(d) && isNamed(name, d)) {
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
                    if (hasMatchingSignature(signature, ellipsis, d)) {
                        //we have found an exactly matching 
                        //overloaded declaration
                        addIfBetterMatch(results, d);
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

    private static void addIfBetterMatch(List<Declaration> results, Declaration d) {
        boolean add=true;
        for (Iterator<Declaration> i = results.iterator(); i.hasNext();) {
            Declaration o = i.next();
            if (betterMatch(d, o)) {
                i.remove();
            }
            else if (betterMatch(o, d)) { //TODO: note asymmetry here resulting in nondeterminate behavior!
                add=false;
            }
        }
        if (add) results.add(d);
    }
    
    public static Declaration findMatchingOverloadedClass(Class abstractionClass, 
    		List<ProducedType> signature, boolean ellipsis) {
        List<Declaration> results = new ArrayList<Declaration>();
        if (!abstractionClass.isAbstraction()) {
            return abstractionClass;
        }
        for (Declaration overloaded: abstractionClass.getOverloads()) {
            if (hasMatchingSignature(signature, ellipsis, overloaded, false)) {
                addIfBetterMatch(results, overloaded);
            }
        }
        if (results.size() == 1) {
            return results.get(0);
        }
        return abstractionClass;
    }

    public static boolean isTypeUnknown(ProducedType type) {
        return type==null || type.getDeclaration()==null ||
                type.getDeclaration() instanceof UnknownType;
    }

    public static List<ProducedType> getSignature(Declaration dec) {
        if(dec instanceof Functional == false)
            return null;
        List<ParameterList> parameterLists = ((Functional)dec).getParameterLists();
        if(parameterLists == null || parameterLists.isEmpty())
            return null;
        ParameterList parameterList = parameterLists.get(0);
        if(parameterList == null || parameterList.getParameters() == null)
            return null;
        int size = parameterList.getParameters().size();
		List<ProducedType> signature = new ArrayList<ProducedType>(size);
        for(Parameter param : parameterList.getParameters()){
            signature.add(param.getType());
        }
        return signature;
    }
    
    public static boolean isCompletelyVisible(Declaration member, ProducedType pt) {
        if (pt.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                if ( !isCompletelyVisible(member, ct.substitute(pt.getTypeArguments())) ) {
                    return false;
                }
            }
            return true;
        }
        else if (pt.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                if ( !isCompletelyVisible(member, ct.substitute(pt.getTypeArguments())) ) {
                    return false;
                }
            }
            return true;
        }
        else {
            if (!isVisible(member, pt.getDeclaration())) {
                return false;
            }
            for (ProducedType at: pt.getTypeArgumentList()) {
                if ( at!=null && !isCompletelyVisible(member, at) ) {
                    return false;
                }
            }
            return true;
        }
    }

    static boolean isVisible(Declaration member, TypeDeclaration type) {
        return type instanceof TypeParameter || 
                type.isVisible(member.getVisibleScope());
    }

	/**
     * Given two instantiations of the same type constructor,
     * construct a principal instantiation that is a supertype
     * of both. This is impossible in the following special
     * cases:
     * 
     * - an abstract class which does not obey the principal
     *   instantiation inheritance rule
     * - an intersection between two instantiations of the
     *   same type where one argument is a type parameter
     * 
     * Nevertheless, we give it our best shot!
     */
    public static List<ProducedType> constructPrincipalInstantiation(
            TypeDeclaration dec, ProducedType first, ProducedType second,
            Unit unit) {
        List<ProducedType> args = new ArrayList<ProducedType>();
        for (TypeParameter tp: dec.getTypeParameters()) {
            List<ProducedType> l = new ArrayList<ProducedType>();
            ProducedType arg;
            ProducedType rta = first.getTypeArguments().get(tp);
            ProducedType prta = second.getTypeArguments().get(tp);
            if (tp.isContravariant()) {
                addToUnion(l, rta);
                addToUnion(l, prta);
                UnionType ut = new UnionType(unit);
                ut.setCaseTypes(l);
                arg = ut.getType();
            }
            else { //if (tp.isCovariant()) {
                addToIntersection(l, rta, unit);
                addToIntersection(l, prta, unit);
                IntersectionType it = new IntersectionType(unit);
                it.setSatisfiedTypes(l);
                arg = it.canonicalize().getType();
            }
//                            else {
//                                if (rta.isExactlyInternal(prta)) {
//                                    arg = rta;
//                                }
//                                else {
//                                    //TODO: think this case through better!
//                                    return null;
//                                }
//                            }
            args.add(arg);
        }
        return args;
    }

}
