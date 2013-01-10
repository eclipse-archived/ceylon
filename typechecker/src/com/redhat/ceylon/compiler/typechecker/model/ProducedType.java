package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isElementOfUnion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A produced type with actual type arguments.
 * This represents something that is actually
 * considered a "type" in the language
 * specification.
 *
 * @author Gavin King
 */
public class ProducedType extends ProducedReference {
    
    private String underlyingType;
    private boolean isRaw;
    private ProducedType resolvedAliases;
    private Map<TypeDeclaration, ProducedType> superTypesCache = new HashMap<TypeDeclaration, ProducedType>();

    ProducedType() {}

    @Override
    public TypeDeclaration getDeclaration() {
        return (TypeDeclaration) super.getDeclaration();
    }
    
    /**
     * Is this type exactly the same type as the
     * given type? 
     */
    public boolean isExactly(ProducedType type) {
        return resolveAliases().isExactlyInternal(type.resolveAliases());
    }
    
    public boolean isExactlyInternal(ProducedType type) {
        if (getDeclaration() instanceof NothingType) {
            return type.getDeclaration() instanceof NothingType;
        }
        else if (getDeclaration() instanceof UnionType) {
            List<ProducedType> cases = getCaseTypes();
            if (type.getDeclaration() instanceof UnionType) {
                List<ProducedType> otherCases = type.getCaseTypes();
                if (cases.size()!=otherCases.size()) {
                    return false;
                }
                else {
                    for (ProducedType c: cases) {
                        boolean found = false;
                        for (ProducedType oc: otherCases) {
                            if (c.isExactlyInternal(oc)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            else if (cases.size()==1) {
                ProducedType st = cases.get(0);
                return st.isExactlyInternal(type);
            }
            else {
                return false;
            }
        }
        else if (getDeclaration() instanceof IntersectionType) {
            List<ProducedType> types = getSatisfiedTypes();
            if (type.getDeclaration() instanceof IntersectionType) {
                List<ProducedType> otherTypes = type.getSatisfiedTypes();
                if (types.size()!=otherTypes.size()) {
                    return false;
                }
                else {
                    for (ProducedType c: types) {
                        boolean found = false;
                        for (ProducedType oc: otherTypes) {
                            if (c.isExactlyInternal(oc)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            else if (types.size()==1) {
                ProducedType st = types.get(0);
                return st.isExactlyInternal(type);
            }
            else {
                return false;
            }
        }
        else if (type.getDeclaration() instanceof UnionType) {
            List<ProducedType> otherCases = type.getCaseTypes();
            if (otherCases.size()==1) {
                ProducedType st = otherCases.get(0);
                return this.isExactlyInternal(st);
            }
            else {
                return false;
            }
        }
        else if (type.getDeclaration() instanceof IntersectionType) {
            List<ProducedType> otherTypes = type.getSatisfiedTypes();
            if (otherTypes.size()==1) {
                ProducedType st = otherTypes.get(0);
                return this.isExactlyInternal(st);
            }
            else {
                return false;
            }
        }
        else {
            if (!type.getDeclaration().equals(getDeclaration())) {
                return false;
            }
            else {
                ProducedType qt = getDeclaration().isStaticallyImportable() ?
                        null : getQualifyingType();
                ProducedType tqt = type.getDeclaration().isStaticallyImportable() ? 
                        null : type.getQualifyingType();
                if (qt==null) {
                    if (tqt!=null) {
                        return false;
                    }
                }
                else {
                    if (tqt==null) {
                        return false;
                    }
                    else {
                        TypeDeclaration totd = (TypeDeclaration) type.getDeclaration().getContainer();
                        ProducedType tqts = tqt.getSupertype(totd);
                        TypeDeclaration otd = (TypeDeclaration) getDeclaration().getContainer();
                        ProducedType qts = qt.getSupertype(otd);
                        if ( !qts.isExactlyInternal(tqts) ) {
                            return false;
                        }
                    }
                }
                for (TypeParameter p: getDeclaration().getTypeParameters()) {
                    ProducedType arg = getTypeArguments().get(p);
                    ProducedType otherArg = type.getTypeArguments().get(p);
                    if (arg==null || otherArg==null) {
                        return false;
                        /*throw new RuntimeException(
                                "Missing type argument for: " +
                                        p.getName() + " of " +
                                        getDeclaration().getName());*/
                    }
                    else if (!arg.isExactlyInternal(otherArg)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    /**
     * Is this type a supertype of the given type? 
     */
    public boolean isSupertypeOf(ProducedType type) {
        return type.isSubtypeOf(this);
    }

    /**
     * Is this type a subtype of the given type? 
     */
    public boolean isSubtypeOf(ProducedType type) {
        return type!=null && resolveAliases().isSubtypeOfInternal(type.resolveAliases());
    }
    
    /**
     * Is this type a subtype of the given type? Ignore
     * a certain self type constraint.
     */
    public boolean isSubtypeOfInternal(ProducedType type) {
        if (getDeclaration() instanceof NothingType) {
            return true;
        }
        else if (type.getDeclaration() instanceof NothingType) {
            return false;
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getInternalCaseTypes()) {
                if (ct==null || !ct.isSubtypeOfInternal(type)) {
                    return false;
                }
            }
            return true;
        }
        else if (type.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: type.getInternalCaseTypes()) {
                if (ct!=null && isSubtypeOfInternal(ct)) {
                    return true;
                }
            }
            return false;
        }
        else if (type.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: type.getInternalSatisfiedTypes()) {
                if (ct!=null && !isSubtypeOfInternal(ct)) {
                    return false;
                }
            }
            return true;
        }
        else if (getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: getInternalSatisfiedTypes()) {
                if (ct==null || ct.isSubtypeOfInternal(type)) {
                    return true;
                }
            }
            return false;
        }
        else {
            ProducedType st = getSupertype(type.getDeclaration());
            if (st==null) {
                return false;
            }
            else {
                ProducedType stqt = st.getDeclaration().isStaticallyImportable() ?
                        null : st.getQualifyingType();
                ProducedType tqt = type.getDeclaration().isStaticallyImportable() ? 
                        null : type.getQualifyingType();
                if (stqt==null) {
                    if (tqt!=null) {
                        //probably extraneous!
                        return false;
                    }
                }
                else {
                    if (tqt==null) {
                        //probably extraneous!
                        return false;
                    }
                    else {
                        //note that the qualifying type of the
                        //given type may be an invariant subtype
                        //of the type that declares the member
                        //type, as long as it doesn't refine the
                        //member type
                        TypeDeclaration totd = (TypeDeclaration) type.getDeclaration().getContainer();
                        ProducedType tqts = tqt.getSupertype(totd);
                        if (!stqt.isSubtypeOf(tqts)) {
                            return false;
                        }
                    }
                }
                for (TypeParameter p: type.getDeclaration().getTypeParameters()) {
                    ProducedType arg = st.getTypeArguments().get(p);
                    ProducedType otherArg = type.getTypeArguments().get(p);
                    if (arg==null || otherArg==null) {
                        /*throw new RuntimeException("Missing type argument for type parameter: " +
                                      p.getName() + " of " +
                                      type.getDeclaration().getName());*/
                        return false;
                    }
                    else if (p.isCovariant()) {
                        if (!arg.isSubtypeOf(otherArg)) {
                            return false;
                        }
                    }
                    else if (p.isContravariant()) {
                        if (!otherArg.isSubtypeOf(arg)) {
                            return false;
                        }
                    }
                    else {
                        if (!arg.isExactlyInternal(otherArg)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
    }

    /**
     * Eliminate the given type from the union type.
     * (Performs a set complement operation.) Note
     * that this operation is not robust and only 
     * works if this is a union of the given type
     * with some other types that don't involve the
     * given type.
     */
    public ProducedType minus(ClassOrInterface ci) {
        if ((getDeclaration() instanceof ClassOrInterface && getDeclaration().equals(ci)) 
                || getSupertype(ci) != null) {
            return getDeclaration().getUnit().getNothingDeclaration().getType();
        }
        else if (getDeclaration() instanceof UnionType) {
            List<ProducedType> types = new ArrayList<ProducedType>();
            for (ProducedType ct: getCaseTypes()) {
                if (ct.getSupertype(ci)==null) {
                    addToUnion(types, ct.minus(ci));
                }
            }
            UnionType ut = new UnionType(getDeclaration().getUnit());
            ut.setCaseTypes(types);
            return ut.getType();
        }
        else {
            return this;
        }
    }

    /**
     * Substitute the given types for the corresponding
     * given type parameters wherever they appear in the
     * type. 
     */
    public ProducedType substitute(Map<TypeParameter, ProducedType> substitutions) {
        return new Substitution().substitute(this, substitutions);
    }

    private ProducedType substituteInternal(Map<TypeParameter, ProducedType> substitutions) {
        return new InternalSubstitution().substitute(this, substitutions);
    }

    /**
     * A member or member type of the type with actual type 
     * arguments to the receiving type and invocation.
     */
    public ProducedReference getTypedReference(Declaration member, 
            List<ProducedType> typeArguments) {
        if (member instanceof TypeDeclaration) {
            return getTypeMember( (TypeDeclaration) member, typeArguments );
        }
        else {
            return getTypedMember( (TypedDeclaration) member, typeArguments);
        }
    }

    /**
     * A member of the type with actual type arguments
     * to the receiving type and invocation.
     * 
     * @param member the declaration of a member of
     *               this type
     * @param typeArguments the type arguments of the
     *                      invocation
     */
    public ProducedTypedReference getTypedMember(TypedDeclaration member, 
            List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype((TypeDeclaration) member.getContainer());
        /*if (declaringType==null) {
            return null;
        }
        else {*/
        ProducedTypedReference ptr = new ProducedTypedReference();
        ptr.setDeclaration(member);
        ptr.setQualifyingType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
        //map.putAll(sub(map));
        ptr.setTypeArguments(map);
        return ptr;
        //}
    }

    /**
     * A member type of the type with actual type arguments
     * to the receiving type and invocation.
     * 
     * @param member the declaration of a member type of
     *               this type
     * @param typeArguments the type arguments of the
     *                      invocation
     */
    public ProducedType getTypeMember(TypeDeclaration member, 
            List<ProducedType> typeArguments) {
        ProducedType declaringType = getSupertype((TypeDeclaration) member.getContainer());
    	return member.getProducedType(declaringType, typeArguments);
        /*ProducedType pt = new ProducedType();
        pt.setDeclaration(member);
        pt.setQualifyingType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
        //map.putAll(sub(map));
        pt.setTypeArguments(map);
        return pt;*/
    }

    /**
     * Substitute invocation type arguments into an upper bound 
     * on a type parameter of the invocation, where this type 
     * represents an upper bound.
     * 
     * @param receiver the type that receives the invocation
     * @param member the invoked member
     * @param typeArguments the explicit or inferred type 
     *                      arguments of the invocation
     * 
     * @return the upper bound of a type parameter, after 
     *         performing all type argument substitution
     */
    public ProducedType getProducedType(ProducedType receiver, Declaration member, 
            List<ProducedType> typeArguments) {
        ProducedType rst = (receiver==null) ? null : 
                receiver.getSupertype((TypeDeclaration) member.getContainer());
        return new Substitution().substitute(this, arguments(member, rst, typeArguments));
    }

    public ProducedType getType() {
        return this;
    }
    
    /**
     * Get all supertypes of the type by traversing the whole
     * type hierarchy. Avoid using this!
     */
    public List<ProducedType> getSupertypes() {
        return getSupertypes(new ArrayList<ProducedType>());
    }
    
    private List<ProducedType> getSupertypes(List<ProducedType> list) {
        if ( isWellDefined() && (getDeclaration() instanceof UnionType || 
                                 getDeclaration() instanceof IntersectionType || 
                                 Util.addToSupertypes(list, this)) ) {
            ProducedType extendedType = getExtendedType();
            if (extendedType!=null) {
                extendedType.getSupertypes(list);
            }
            for (ProducedType dst: getSatisfiedTypes()) {
                dst.getSupertypes(list);
            }
            if (getDeclaration() instanceof UnionType) {
                //trying to infer supertypes of algebraic
                //types from their cases was resulting in
                //stack overflows and is not currently 
                //required by the spec
                List<ProducedType> caseTypes = getCaseTypes();
                if (caseTypes!=null) {
                    for (ProducedType t: caseTypes) {
                    	if (!alreadyInList(list, t)) {
                    		List<ProducedType> candidates = t.getSupertypes();
                    		for (ProducedType st: candidates) {
                    			boolean include = true;
                    			for (ProducedType ct: getDeclaration().getCaseTypes()) {
                    				if (!ct.isSubtypeOf(st)) {
                    					include = false;
                    					break;
                    				}
                    			}
                    			if (include) {
                    				Util.addToSupertypes(list, st);
                    			}
                    		}
                    	}
                    }
                }
            }
        }
        return list;
    }

	private boolean alreadyInList(List<ProducedType> list, ProducedType t) {
		for (ProducedType pt: list) {
			if (t.getDeclaration() instanceof UnionType ||
				t.getDeclaration() instanceof IntersectionType ||
				(t.getDeclaration().equals(pt.getDeclaration()) && 
					t.isExactlyInternal(pt))) {
				return true;
			}
		}
		return false;
	}
    
    /**
     * Given a type declaration, return a produced type of 
     * which this type is an invariant subtype.
     * 
     * @param dec a type declaration
     * 
     * @return a produced type of the given type declaration
     *         which is a supertype of this type, or null if
     *         there is no such supertype
     */
    public ProducedType getSupertype(final TypeDeclaration dec) {
        boolean complexType = dec instanceof UnionType || dec instanceof IntersectionType;
        if(!complexType && superTypesCache.containsKey(dec)){
            return superTypesCache.get(dec);
        }
        Criteria c = new Criteria() {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                return !(type instanceof UnionType) && 
                       !(type instanceof IntersectionType) && 
                       type.equals(dec);
            }
            @Override
            public boolean isMemberLookup() {
            	return false;
            }
        };
        ProducedType superType = getSupertype(c, new ArrayList<ProducedType>());
        if (!complexType) superTypesCache.put(dec, superType);
        return superType;
    }
    
    /**
     * Given a predicate, return a produced type for a 
     * declaration satisfying the predicate, of which 
     * this type is an invariant subtype. 
     */
    ProducedType getSupertype(Criteria c) {
        return getSupertype(c, new ArrayList<ProducedType>());
    }
    
    static interface Criteria {
        boolean satisfies(TypeDeclaration type);
        boolean isMemberLookup();
    }
    
    /**
     * Search for the most-specialized supertype 
     * satisfying the given predicate. 
     */
    private ProducedType getSupertype(final Criteria c, List<ProducedType> list) {
        if (c.satisfies(getDeclaration())) {
            return qualifiedByDeclaringType();
        }
        if ( isWellDefined() && (getDeclaration() instanceof UnionType || 
                                 getDeclaration() instanceof IntersectionType || 
                                 Util.addToSupertypes(list, this)) ) {
            //now let's call the two most difficult methods
            //in the whole code base:
            ProducedType result = getPrincipalInstantiation(c, list);
            result = getPrincipalInstantiationFromCases(c, list, result);
            return result;
        }
        else {
            return null;
        }
    }
    
    private ProducedType getPrincipalInstantiationFromCases(final Criteria c,
            List<ProducedType> list, ProducedType result) {
        if (getDeclaration() instanceof UnionType) {
            //trying to infer supertypes of algebraic
            //types from their cases was resulting in
            //stack overflows and is not currently 
            //required by the spec
            final List<ProducedType> caseTypes = getInternalCaseTypes();
            if (caseTypes!=null && !caseTypes.isEmpty()) {
                //first find a common superclass or superinterface 
                //declaration that satisfies the criteria, ignoring
                //type arguments for now
                ProducedType stc = findCommonSuperclass(c, caseTypes);
                if (stc!=null) {
                    //we found the declaration, now try to construct a 
                    //produced type that is a true common supertype
                    ProducedType candidateResult = getCommonSupertype(caseTypes, 
                            stc.getDeclaration());
                    if (candidateResult!=null && (result==null || 
                            candidateResult.isSubtypeOfInternal(result))) {
                        result = candidateResult;
                    }
                }
            }
        }
        return result;
    }

	private ProducedType findCommonSuperclass(Criteria c,
			List<ProducedType> types) {
		ProducedType stc = null;
		for (ProducedType pt: types.get(0).getSupertypes()) {
			TypeDeclaration ptd = pt.getDeclaration();
			if (ptd instanceof ClassOrInterface && c.satisfies(ptd)) {
		    	for (ProducedType ct: types) {
		    		if (!ct.getDeclaration().inherits(ptd)) {
		    			pt = null;
		    			break;
		    		}
		    	}
		    	if (pt!=null) {
		    		if (stc==null) {
		    			stc = pt;
		    		}
		    		else if (ptd.inherits(stc.getDeclaration())) {
		    			stc = pt;
		    		}
		    		else if (stc.getDeclaration().inherits(ptd)) {
		    			
		    		}
		    		else {
		    			stc = null;
		    			break;
		    		}
		    	}
			}
		}
		return stc;
	}

    private ProducedType getPrincipalInstantiation(Criteria c,
            List<ProducedType> list) {
        //search for the most-specific supertype 
        //for the given declaration
        
        ProducedType result = null;
        
        ProducedType extendedType = getInternalExtendedType();
        if (extendedType!=null) {
            ProducedType possibleResult = extendedType.getSupertype(c, list);
            if (possibleResult!=null) {
                result = possibleResult;
            }
        }
        
        List<ProducedType> satisfiedTypes = getInternalSatisfiedTypes();
		for (ProducedType dst: satisfiedTypes) {
            ProducedType possibleResult = dst.getSupertype(c, list);
            if (possibleResult!=null) {
                if (result==null || possibleResult.isSubtypeOfInternal(result)) {
                    result = possibleResult;
                }
                else if ( !result.isSubtypeOfInternal(possibleResult) ) {
                    //TODO: this is still needed even though we keep intersections 
                    //      in canonical form because you can have stuff like
                    //      empty of Iterable<String>&Sized
                    TypeDeclaration rd = result.getDeclaration();
                    TypeDeclaration prd = possibleResult.getDeclaration();

                    //Resolve ambiguities in favor of
                    //the most-refined declaration
                    /*if (rd.equals(prd)) {
                        List<ProducedType> args = constructPrincipalInstantiation(
                                rd, result, possibleResult);
                        //TODO: broken for member types! ugh :-(
                        result = rd.getProducedType(result.getQualifyingType(), args);
                    }
                    else if (rd.inherits(prd)) {
                    }
                    else if (prd.inherits(rd)) {
                    	result = possibleResult;
                    }*/
                    
                    TypeDeclaration d = null;
                    if (rd.equals(prd)) {
                    	d = rd;
                    }
                    //Resolve ambiguities in favor of 
                    //least-refined declaration (in
                    //order to take advantage of most
                    //specific type arguments)
                    /*else if (rd.inherits(prd)) {
                    	d=prd;
                    	result=result.getSupertype(d);
                    }
                    else if (prd.inherits(rd)) {
                    	d=rd;
                    	possibleResult=possibleResult.getSupertype(d);
                    }*/
                    
                    if (d!=null) {
                        List<ProducedType> args = constructPrincipalInstantiation(
                                d, result, possibleResult);
                        //TODO: broken for member types! ugh :-(
                        result = d.getProducedType(result.getQualifyingType(), args);
                    }
                    else {
                        //ambiguous! we can't decide between the two 
                        //supertypes which both satisfy the criteria
                        if (c.isMemberLookup() && !satisfiedTypes.isEmpty()) {
                        	//for the case of a member lookup, try to find
                        	//a common supertype by forming the union of 
                        	//the two possible results (since A|B is always
                        	//a supertype of A&B)
                        	UnionType ut = new UnionType(getDeclaration().getUnit());
                        	List<ProducedType> caseTypes = new ArrayList<ProducedType>();
                        	//if (extendedType!=null) caseTypes.add(extendedType);
                        	//caseTypes.addAll(satisfiedTypes);
                        	caseTypes.add(result);
                        	caseTypes.add(possibleResult);
                        	ut.setCaseTypes(caseTypes);
                        	result = ut.getType().getSupertype(c, list);
                        	if (result==null) {
                            	return new UnknownType(getDeclaration().getUnit()).getType();
                        	}
                        }
                        else {
                        	return new UnknownType(getDeclaration().getUnit()).getType();
                        }
                    }
                }
            }
        }
		
        return result;
    }

    /**
     * Given two instantiations of the same type declaration,
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
    private List<ProducedType> constructPrincipalInstantiation(
            TypeDeclaration dec, ProducedType first, ProducedType second) {
        List<ProducedType> args = new ArrayList<ProducedType>();
        for (TypeParameter tp: dec.getTypeParameters()) {
            List<ProducedType> l = new ArrayList<ProducedType>();
            Unit unit = getDeclaration().getUnit();
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
            else {//if (tp.isCovariant()) {
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
    
    private ProducedType qualifiedByDeclaringType() {
        ProducedType qt = getQualifyingType();
        if (qt==null) {
            return this;
        }
        else {
            ProducedType pt = new ProducedType();
            pt.setDeclaration(getDeclaration());
            pt.setTypeArguments(getTypeArguments());
            //replace the qualifying type with
            //the supertype of the qualifying 
            //type that declares this nested
            //type, substituting type arguments
            ProducedType declaringType = qt.getSupertype((TypeDeclaration) getDeclaration().getContainer());
            pt.setQualifyingType(declaringType);
            return pt;
        }
    }

    private ProducedType getCommonSupertype(final List<ProducedType> caseTypes,
            TypeDeclaration dec) {
        //now try to construct a common produced
        //type that is a common supertype by taking
        //the type args and unioning them
        List<ProducedType> args = new ArrayList<ProducedType>();
        for (TypeParameter tp: dec.getTypeParameters()) {
            List<ProducedType> list2 = new ArrayList<ProducedType>();
            ProducedType result;
            if (tp.isContravariant()) { 
                for (ProducedType pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    ProducedType st = pt.getSupertype(dec);
                    if (st==null) {
                        return null;
                    }
                    addToIntersection(list2, st.getTypeArguments().get(tp), getDeclaration().getUnit());
                }
                IntersectionType it = new IntersectionType(getDeclaration().getUnit());
                it.setSatisfiedTypes(list2);
                result = it.canonicalize().getType();
            }
            else {
                for (ProducedType pt: caseTypes) {
                    if (pt==null) {
                        return null;
                    }
                    ProducedType st = pt.getSupertype(dec);
                    if (st==null) {
                        return null;
                    }
                    addToUnion(list2, st.getTypeArguments().get(tp));
                }
                UnionType ut = new UnionType(getDeclaration().getUnit());
                ut.setCaseTypes(list2);
                result = ut.getType();
            }
            args.add(result);
        }
        //check that the unioned type args
        //satisfy the type constraints
        //disabled this according to #52
        /*for (int i=0; i<args.size(); i++) {
            TypeParameter tp = dec.getTypeParameters().get(i);
            for (ProducedType ub: tp.getSatisfiedTypes()) {
                if (!args.get(i).isSubtypeOf(ub)) {
                    return null;
                }
            }
        }*/
        //recurse to the qualifying type
        ProducedType outerType;
        if (dec.isMember()) {
            TypeDeclaration outer = (TypeDeclaration) dec.getContainer();
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType pt: caseTypes) {
                if (pt==null) {
                    return null;
                }
                ProducedType st = pt.getQualifyingType().getSupertype(outer);
                list.add(st);
            }
            outerType = getCommonSupertype(list, outer);
        }
        else {
            outerType = null;
        }
        //make the resulting type
        ProducedType candidateResult = dec.getProducedType(outerType, args);
        //check the the resulting type is *really*
        //a subtype (take variance into account)
        for (ProducedType pt: caseTypes) {
            if (!pt.isSubtypeOf(candidateResult)) {
                return null;
            }
        }
        return candidateResult;
    }
    
    /**
     * Get the type arguments as a tuple. 
     */
    public List<ProducedType> getTypeArgumentList() {
        List<ProducedType> lpt = new ArrayList<ProducedType>();
        for (TypeParameter tp : getDeclaration().getTypeParameters()) {
            lpt.add(getTypeArguments().get(tp));
        }
        return lpt;
    }

    /**
     * Determine if this is a decidable supertype, i.e. if it
     * obeys the restriction that types with contravariant 
     * type parameters may only appear in covariant positions. 
     * 
     * @return a list of type parameters which appear in illegal 
     *         positions
     */
    public List<TypeDeclaration> checkDecidability() {
        List<TypeDeclaration> errors = new ArrayList<TypeDeclaration>();
        for (TypeParameter tp: getDeclaration().getTypeParameters()) {
            ProducedType pt = getTypeArguments().get(tp);
            if (pt!=null) {
                pt.checkDecidability(tp.isCovariant(), tp.isContravariant(), errors);
            }
        }
        return errors;
    }
    
    private void checkDecidability(boolean covariant, boolean contravariant,
            List<TypeDeclaration> errors) {
        if (getDeclaration() instanceof TypeParameter) {
            //nothing to do
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                ct.checkDecidability(covariant, contravariant, errors);
            }
        }
        else if (getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: getSatisfiedTypes()) {
                ct.checkDecidability(covariant, contravariant, errors);
            }
        }
        else {
            for (TypeParameter tp: getDeclaration().getTypeParameters()) {
                if (!covariant && tp.isContravariant()) {
                    //a type with contravariant parameters appears at
                    //a contravariant location in satisfies / extends
                    errors.add(getDeclaration());
                }
                ProducedType pt = getTypeArguments().get(tp);
                if (pt!=null) {
                    if (tp.isCovariant()) {
                        pt.checkDecidability(covariant, contravariant, errors);
                    }
                    else if (tp.isContravariant()) {
                        if (covariant|contravariant) { 
                            pt.checkDecidability(!covariant, !contravariant, errors); 
                        }
                        else {
                            //else if we are in a nonvariant position, it stays nonvariant
                            pt.checkDecidability(covariant, contravariant, errors);
                        }
                    }
                    else {
                        pt.checkDecidability(false, false, errors);
                    }
                }
            }
        }
    }
    
    /**
     * Check that this type can appear at a position,
     * given the variance of the position (covariant,
     * contravariant, or invariant.)
     * 
     * @param covariant true for a covariant position
     * @param contravariant true for a contravariant 
     *                      position
     * @param declaration TODO!
     * 
     * @return a list of type parameters which appear
     *         in illegal positions
     */
    public List<TypeParameter> checkVariance(boolean covariant, boolean contravariant,
            Declaration declaration) {
        List<TypeParameter> errors = new ArrayList<TypeParameter>();
        checkVariance(covariant, contravariant, declaration, errors);
        return errors;
    }
    
    private void checkVariance(boolean covariant, boolean contravariant,
                Declaration declaration, List<TypeParameter> errors) {
        //TODO: fix this to allow reporting multiple errors!
        if (getDeclaration() instanceof TypeParameter) {
            TypeParameter tp = (TypeParameter) getDeclaration();
            boolean ok = tp.getDeclaration().equals(declaration) ||
                    ((covariant || !tp.isCovariant()) && 
                            (contravariant || !tp.isContravariant()));
            if (!ok) {
                //a covariant type parameter appears in a contravariant location, or
                //a contravariant type parameter appears in a covariant location.
                errors.add(tp);
            }
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getCaseTypes()) {
                ct.checkVariance(covariant, contravariant, declaration, errors);
            }
        }
        else if (getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: getSatisfiedTypes()) {
                ct.checkVariance(covariant, contravariant, declaration, errors);
            }
        }
        else {
            if (getQualifyingType()!=null) {
                getQualifyingType().checkVariance(covariant, contravariant, declaration, errors);
            }
            for (TypeParameter tp: getDeclaration().getTypeParameters()) {
                ProducedType pt = getTypeArguments().get(tp);
                if (pt!=null) {
                    if (tp.isCovariant()) {
                        pt.checkVariance(covariant, contravariant, declaration, errors);
                    }
                    else if (tp.isContravariant()) {
                        if (covariant|contravariant) { 
                            pt.checkVariance(!covariant, !contravariant, declaration, errors); 
                        }
                        else {
                            //else if we are in a nonvariant position, it stays nonvariant
                            pt.checkVariance(covariant, contravariant, declaration, errors);
                        }
                    }
                    else {
                        pt.checkVariance(false, false, declaration, errors);
                    }
                }
            }
        }
    }

    /**
     * Is the type welldefined? Are any of its arguments
     * garbage unknown types?
     */
    public boolean isWellDefined() {
        for (ProducedType at: getTypeArgumentList()) {
            if (at==null || !at.isWellDefined() ) {
                return false;
            }
        }
        return true;
    }

    private List<ProducedType> getInternalSatisfiedTypes() {
        List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
        for (ProducedType st: getDeclaration().getSatisfiedTypes()) {
            satisfiedTypes.add(st.substituteInternal(getTypeArguments()));
        }
        return satisfiedTypes;
    }

    private ProducedType getInternalExtendedType() {
        ProducedType extendedType = getDeclaration().getExtendedType();
        return extendedType==null?null:extendedType.substituteInternal(getTypeArguments());
    }

    private List<ProducedType> getInternalCaseTypes() {
        if (getDeclaration().getCaseTypes()==null) {
            return null;
        }
        else {
            List<ProducedType> caseTypes = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                caseTypes.add(ct.substituteInternal(getTypeArguments()));
            }
            return caseTypes;
        }
    }

    private List<ProducedType> getSatisfiedTypes() {
        List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
        Map<TypeParameter, ProducedType> args = getTypeArguments();
        for (ProducedType satisfiedType: getDeclaration().getSatisfiedTypes()) {
            satisfiedTypes.add(args.isEmpty() ? satisfiedType : satisfiedType.substitute(args));
        }
        return satisfiedTypes;
    }

    private ProducedType getExtendedType() {
        ProducedType extendedType = getDeclaration().getExtendedType();
        if (extendedType==null) {
            return null;
        }
        else {
            Map<TypeParameter, ProducedType> args = getTypeArguments();
            return args.isEmpty() ? extendedType : extendedType.substitute(args);
        }
    }

    public List<ProducedType> getCaseTypes() {
        if (getDeclaration().getCaseTypes()==null) {
            return null;
        }
        else {
            List<ProducedType> caseTypes = new ArrayList<ProducedType>();
            for (ProducedType ct: getDeclaration().getCaseTypes()) {
                caseTypes.add(ct.substitute(getTypeArguments()));
            }
            return caseTypes;
        }
    }

    /**
     * Substitutes type arguments for type parameters.
     * This default strategy eliminates duplicate types
     * from unions after substituting arguments.
     * @author Gavin King
     */
    static class Substitution {
        
        ProducedType substitute(ProducedType pt, 
                Map<TypeParameter, ProducedType> substitutions) {
            Declaration dec;
            if (pt.getDeclaration() instanceof UnionType) {
                UnionType ut = new UnionType(pt.getDeclaration().getUnit());
                List<ProducedType> types = new ArrayList<ProducedType>();
                for (ProducedType ct: pt.getDeclaration().getCaseTypes()) {
                    addTypeToUnion(ct, substitutions, types);
                }
                ut.setCaseTypes(types);
                dec = ut;
            }
            else if (pt.getDeclaration() instanceof IntersectionType) {
                IntersectionType it = new IntersectionType(pt.getDeclaration().getUnit());
                List<ProducedType> types = new ArrayList<ProducedType>();
                for (ProducedType ct: pt.getDeclaration().getSatisfiedTypes()) {
                    addTypeToIntersection(ct, substitutions, types);
                }
                it.setSatisfiedTypes(types);
                dec = it.canonicalize();
            }
            else {
                if (pt.getDeclaration() instanceof TypeParameter) {
                    ProducedType sub = substitutions.get(pt.getDeclaration());
                    if (sub!=null) {
                        return sub;
                    }
                }
                dec = pt.getDeclaration();
            }
            return substitutedType(dec, pt, substitutions);
        }

        void addTypeToUnion(ProducedType ct, Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct==null) {
                types.add(null);
            }
            else {
                addToUnion(types, substitute(ct, substitutions));
            }
        }

        void addTypeToIntersection(ProducedType ct, Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct==null) {
                types.add(null);
            }
            else {
                addToIntersection(types, substitute(ct, substitutions), ct.getDeclaration().getUnit());
            }
        }

        private Map<TypeParameter, ProducedType> substitutedTypeArguments(ProducedType pt, 
                Map<TypeParameter, ProducedType> substitutions) {
            Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
            for (Map.Entry<TypeParameter, ProducedType> e: pt.getTypeArguments().entrySet()) {
                if (e.getValue()!=null) {
                    map.put(e.getKey(), substitute(e.getValue(), substitutions));
                }
            }
            /*ProducedType dt = pt.getDeclaringType();
            if (dt!=null) {
                map.putAll(substituted(dt, substitutions));
            }*/
            return map;
        }

        private ProducedType substitutedType(Declaration dec, ProducedType pt,
                Map<TypeParameter, ProducedType> substitutions) {
            ProducedType type = new ProducedType();
            type.setDeclaration(dec);
            type.setUnderlyingType(pt.getUnderlyingType());
            ProducedType qt = pt.getQualifyingType();
            if (qt!=null) {
                type.setQualifyingType(substitute(qt, substitutions));
            }
            type.setTypeArguments(substitutedTypeArguments(pt, substitutions));
            return type;
        }
            
    }
    
    /**
     * This special strategy for internal use by the 
     * containing class does not eliminate duplicate 
     * types from unions after substituting arguments.
     * This is to avoid a stack overflow that otherwise
     * results! (Determining if a union contains 
     * duplicates requires recursion to the argument
     * substitution code via some very difficult-to-
     * understand flow.)
     * @author Gavin King
     */
    static class InternalSubstitution extends Substitution {
    
        private void addType(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            if (ct!=null) {
                types.add(substitute(ct, substitutions));
            }
        }
        
        @Override void addTypeToUnion(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            addType(ct, substitutions, types);
        }

        @Override void addTypeToIntersection(ProducedType ct,
                Map<TypeParameter, ProducedType> substitutions,
                List<ProducedType> types) {
            addType(ct, substitutions, types);
        }
        
    }

    @Override
    public String toString() {
        return "Type[" + getProducedTypeName() + "]";
    }
    
    public String getProducedTypeName() {
        return getProducedTypeName(true, null);
    }

    public String getProducedTypeName(Unit unit) {
        return getProducedTypeName(true, unit);
    }
    
    public String getProducedTypeName(boolean abbreviate) {
        return getProducedTypeName(abbreviate, null);
    }

    public String getProducedTypeName(boolean abbreviate, Unit unit) {
        if (getDeclaration()==null) {
            return "unknown";
        }
        else {
            if (abbreviate) {
                Unit u = getDeclaration().getUnit();
                if (abbreviateOptional()) {
                	ProducedType dt = minus(u.getNullDeclaration());
                	if (!dt.isPrimitiveAbbreviatedType()) {
                		return "<" + dt.getProducedTypeName(unit) + ">?";
                	}
                	else {
                		return dt.getProducedTypeName(unit) + "?";
                	}
                }
                if (abbreviateSequence()) {
                    ProducedType it = u.getIteratedType(this);
                    if (!it.isPrimitiveAbbreviatedType()) {
                    	return "<" + it.getProducedTypeName(unit) + ">[]";
                    }
                    else {
                    	return it.getProducedTypeName(unit) + "[]";
                    }
                }
                if (abbreviateIterable()) {
                    ProducedType it = u.getIteratedType(this);
                    return "{" + it.getProducedTypeName(unit) + "...}";
                }
                if (abbreviateEmpty()) {
                	return "[]";
                }
                if (abbreviateEntry()) {
                    return u.getKeyType(this).getProducedTypeName(unit) + "->"
                            + u.getValueType(this).getProducedTypeName(unit);
                }
                if (abbreviateCallable()) {
                    List<ProducedType> tal = getTypeArgumentList();
                    ProducedType rt = tal.get(0);
                    String argtypes = argtypes(tal.get(1), unit);
                    if (rt!=null && argtypes!=null) {
                    	if (!rt.isPrimitiveAbbreviatedType()) {
                        	return "<" + rt.getProducedTypeName(unit) + ">" + 
                        			"(" + argtypes + ")";
                    	}
                    	else {
                    		return rt.getProducedTypeName(unit) + 
                    				"(" + argtypes + ")";
                    	}
                    }
                }
                if (abbreviateTuple()) {
                	String argtypes = argtypes(this, unit);
                	if (argtypes!=null) {
                		return "[" + argtypes + "]";
                	}
                }
            }
            if (getDeclaration() instanceof UnionType) {
                StringBuilder name = new StringBuilder();
                for (ProducedType pt: getCaseTypes()) {
                    if (pt==null) {
                        name.append("unknown");
                    }
                    else if (abbreviate && pt.abbreviateEntry()) {
                        name.append("<").append(pt.getProducedTypeName(unit)).append(">");
                    }
                    else {
                        name.append(pt.getProducedTypeName(abbreviate, unit));
                    }
                    name.append("|");
                }
                return name.substring(0,name.length()>0?name.length()-1:0);
            }
            else if (getDeclaration() instanceof IntersectionType) {
                StringBuilder name = new StringBuilder();
                for (ProducedType pt: getSatisfiedTypes()) {
                    if (pt==null) {
                        name.append("unknown");
                    }
                    else if (abbreviate && pt.abbreviateEntry() || 
                    		pt.getDeclaration() instanceof UnionType) {
                        name.append("<").append(pt.getProducedTypeName(abbreviate, unit)).append(">");
                    }
                    else {
                        name.append(pt.getProducedTypeName(abbreviate, unit));
                    }
                    name.append("&");
                }
                return name.substring(0,name.length()>0?name.length()-1:0);
            }
            else {            
                return getSimpleProducedTypeName(abbreviate, unit);
            }
        }
    }

	private boolean abbreviateTuple() {
		return getDeclaration() instanceof Class && 
				getDeclaration().equals(getDeclaration().getUnit()
						.getTupleDeclaration());
	}
    
    private static String argtypes(ProducedType args, Unit unit) {
    	if (args!=null) {
        	Unit u = args.getDeclaration().getUnit();
			boolean defaulted=false;
			if (args.getDeclaration() instanceof UnionType) {
	        	List<ProducedType> cts = args.getDeclaration().getCaseTypes();
	        	if (cts.size()==2) {
	        		TypeDeclaration lc = cts.get(0).getDeclaration();
					if (lc instanceof Interface && 
							lc.equals(u.getEmptyDeclaration())) {
						args = cts.get(1);
	        			defaulted = true;
	        		}
	        		TypeDeclaration rc = cts.get(1).getDeclaration();
					if (lc instanceof Interface &&
							rc.equals(u.getEmptyDeclaration())) {
						args = cts.get(0);
	        			defaulted = true;
	        		}
	        	}
			}
    		if (args.getDeclaration() instanceof ClassOrInterface) {
    			if (args.getDeclaration().equals(u.getTupleDeclaration())) {
    				List<ProducedType> tal = args.getTypeArgumentList();
    				if (tal.size()>=3) {
    					ProducedType first = tal.get(1);
    					ProducedType rest = tal.get(2);
    					if (first!=null && rest!=null) {
    						String argtype = first.getProducedTypeName(unit);
    						if (rest.getDeclaration() instanceof Interface &&
    								rest.getDeclaration().equals(u.getEmptyDeclaration())) {
    							return defaulted ? argtype + "=" : argtype;
    						}
    						String argtypes = argtypes(rest, unit);
    						if (argtypes!=null) {
    							return defaulted ? 
    									argtype + "=, " + argtypes : 
    									argtype + ", " + argtypes;
    						}
    					}
    				}
    			}
    			else if (args.getDeclaration().equals(u.getEmptyDeclaration())) {
    				return defaulted ? "=" : "";
    			}
    			else if (!defaulted && args.getDeclaration().equals(u.getSequentialDeclaration())) {
    				ProducedType elementType = u.getIteratedType(args);
    				if (elementType!=null) { 
    					return elementType.getProducedTypeName(unit) + "...";
    				}
    			}
    		}
    	}
    	return null;
    }

    private boolean abbreviateEntry() {
        Unit unit = getDeclaration().getUnit();
        if (getDeclaration() instanceof Class &&
            getDeclaration().equals(unit.getEntryDeclaration()) &&
                getTypeArgumentList().size()==2) {
            ProducedType kt = unit.getKeyType(this);
            ProducedType vt = unit.getValueType(this);
            return kt!=null && vt!=null; /*&&
                    kt.isPrimitiveAbbreviatedType() && 
                    vt.isPrimitiveAbbreviatedType();*/
        }
        else {
            return false;
        }
    }

    private boolean abbreviateCallable() {
        return getDeclaration() instanceof Interface &&
                getDeclaration().equals(getDeclaration().getUnit().getCallableDeclaration()) &&
                getTypeArgumentList().size()>0 && getTypeArgumentList().get(0)!=null &&
//                getTypeArgumentList().get(0).isPrimitiveAbbreviatedType() &&
                getTypeArgumentList().size()==getDeclaration().getTypeParameters().size();
    }

    private boolean abbreviateSequence() {
    	if (getDeclaration() instanceof Interface) {
    		Unit unit = getDeclaration().getUnit();
    		if (getDeclaration().equals(unit.getSequentialDeclaration())) {
    			ProducedType et = unit.getIteratedType(this);
				return et!=null;// && et.isPrimitiveAbbreviatedType();
    		}
    	}
    	return false;
    }

    private boolean abbreviateIterable() {
    	if (getDeclaration() instanceof Interface) {
    		Unit unit = getDeclaration().getUnit();
    		if (getDeclaration().equals(unit.getIterableDeclaration())) {
    			ProducedType et = unit.getIteratedType(this);
				return et!=null;// && et.isPrimitiveAbbreviatedType();
    		}
    	}
    	return false;
    }

    private boolean abbreviateEmpty() {
    	if (getDeclaration() instanceof Interface) {
    		Unit unit = getDeclaration().getUnit();
    		return getDeclaration().equals(unit.getEmptyDeclaration());
    	}
    	return false;
    }
    
    private boolean abbreviateOptional() {
        if (getDeclaration() instanceof UnionType) {
            Unit unit = getDeclaration().getUnit();
            UnionType ut = (UnionType) getDeclaration();
            return ut.getCaseTypes().size()==2 &&
                    isElementOfUnion(ut, unit.getNullDeclaration()); /*&&
                    minus(unit.getNullDeclaration()).isPrimitiveAbbreviatedType();*/
        }
        else {
            return false;
        }
    }

    private boolean isPrimitiveAbbreviatedType() {
        if (getDeclaration() instanceof IntersectionType) {
            return false;
        }
        else if (getDeclaration() instanceof UnionType) {
            return abbreviateOptional();
        }
        else {
            return !abbreviateEntry();
        }
    }
    
    private String getSimpleProducedTypeName(boolean abbreviate, Unit unit) {
        StringBuilder ptn = new StringBuilder();
        //if (getDeclaration().isMember()) {
        ProducedType qt = getQualifyingType();
        if (qt!=null) {
            ptn.append(qt.getProducedTypeName(abbreviate, unit))
                .append(".");
        }
        //}
        ptn.append(getDeclaration().getName(unit));
        if (!getTypeArgumentList().isEmpty()) {
            ptn.append("<");
            boolean first = true;
            for (ProducedType t: getTypeArgumentList()) {
                if (first) {
                    first = false;
                }
                else {
                    ptn.append(",");
                }
                if (t==null) {
                    ptn.append("unknown");
                }
                else {
                    ptn.append(t.getProducedTypeName(abbreviate, unit));
                }
            }
            ptn.append(">");
        }
        return ptn.toString();
    }

    private String getSimpleProducedTypeQualifiedName() {
        StringBuilder ptn = new StringBuilder();
        //if (getDeclaration().isMember()) {
        ProducedType qt = getQualifyingType();
        if (qt!=null) {
            ptn.append(qt.getProducedTypeQualifiedName())
            .append(".").append(getDeclaration().getName());
        }
        //}
        else {
            ptn.append(getDeclaration().getQualifiedNameString());
        }
        if (!getTypeArgumentList().isEmpty()) {
            ptn.append("<");
            boolean first = true;
            for (ProducedType t: getTypeArgumentList()) {
                if (first) {
                    first = false;
                }
                else {
                    ptn.append(",");
                }
                if (t==null) {
                    ptn.append("?");
                }
                else {
                    ptn.append(t.getProducedTypeQualifiedName());
                }
            }
            ptn.append(">");
        }
        return ptn.toString();
    }

    public String getProducedTypeQualifiedName() {
        if (getDeclaration()==null) {
            //unknown type
            return null;
        }
        if (getDeclaration() instanceof UnionType) {
            StringBuilder name = new StringBuilder();
            for (ProducedType pt: getCaseTypes()) {
                if (pt==null) {
                    name.append("unknown");
                }
                else {
                    name.append(pt.getProducedTypeQualifiedName());
                }
                name.append("|");
            }
            return name.substring(0,name.length()>0?name.length()-1:0);
        }
        else if (getDeclaration() instanceof IntersectionType) {
            StringBuilder name = new StringBuilder();
            for (ProducedType pt: getSatisfiedTypes()) {
                if (pt==null) {
                    name.append("unknown");
                }
                else {
                    name.append(pt.getProducedTypeQualifiedName());
                }
                name.append("&");
            }
            return name.substring(0,name.length()>0?name.length()-1:0);
        }
        else {            
            return getSimpleProducedTypeQualifiedName();
        }
    }

    /**
     * Form a union type of all cases of the type, 
     * recursively reducing cases to their cases
     */
    public ProducedType getUnionOfCases(boolean typeParams) {
        TypeDeclaration sdt = getDeclaration();
        Unit unit = getDeclaration().getUnit();
        //if X is an intersection type A&B, and A is an
        //enumerated type with cases U and V, then the cases
        //of X are the intersection (U|V)&B canonicalized to
        //the union U&B|V&B
        if (sdt instanceof IntersectionType) {
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType st: sdt.getSatisfiedTypes()) {
                addToIntersection(list, st.getUnionOfCases(typeParams)
                        .substitute(getTypeArguments()), 
                        unit); //argument substitution is unnecessary
            }
            IntersectionType it = new IntersectionType(unit);
            it.setSatisfiedTypes(list);
            return it.canonicalize().getType();
        }
        /*if (switchType.getDeclaration() instanceof UnionType) {
            //this branch is not really necessary, because it
            //does basically the same thing as the else clause
            //but it's slightly simpler because there are no 
            //type arguments to substitute
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType st: switchType.getDeclaration().getCaseTypes()) {
                addToUnion(list, getUnionOfCases(st));
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(list);
            return ut.getType();
        }*/
        //if X is neither a union, intersection, or enumerated
        //type then its cases are simply X
        else if (sdt.getCaseTypes()==null) {
            return this;
        }
        else if (sdt instanceof TypeParameter && !typeParams) {
            return this;
        }
        //otherwise, if X is a union A|B, or an enumerated 
        //type, with cases A and B, and A is an enumerated 
        //type with cases U and V, ten the cases of X are
        //the union U|V|B
        else {
            //build a union of all the cases
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType ct: sdt.getCaseTypes()) {
                addToUnion(list, ct.substitute(getTypeArguments())
                        .getUnionOfCases(typeParams)); //note recursion
            }
            UnionType ut = new UnionType(unit);
            ut.setCaseTypes(list);
            return ut.getType();
        }
    }
    
    public void setUnderlyingType(String underlyingType) {
        this.underlyingType = underlyingType;
        // if we have a resolvedAliases cache, update it too
        if(resolvedAliases != null && resolvedAliases != this)
            resolvedAliases.setUnderlyingType(underlyingType);
    }
    
    public String getUnderlyingType() {
        return underlyingType;
    }

    /**
     * Does type covers the given type?
     */
    public boolean covers(ProducedType st) {
        //X covers Y if the union of cases of Y is 
        //a subtype of X
        if (st.getUnionOfCases(true).isSubtypeOf(this)) {
            return true;
        }
        else {
            //X covers Y if Y extends Z and X covers Z
            ProducedType et = st.getDeclaration().getExtendedType();
            if (et!=null && covers(et.substituteInternal(st.getTypeArguments()))) {
                return true;
            }
            //X covers Y if Y satisfies Z and X covers Z
            for (ProducedType pt: st.getDeclaration().getSatisfiedTypes()) {
                if (covers(pt.substituteInternal(st.getTypeArguments()))) {
                    return true;
                }
            }
            //X covers Y if Y is a union type A|B|C and X 
            //covers all of A, B, and C
            //NOTE: we don't apply the same rule for enumerated
            //      types because of decidability problems
            if (st.getDeclaration() instanceof UnionType) {
                for (ProducedType pt: st.getDeclaration().getCaseTypes()) {
                    if (!covers(pt.substituteInternal(st.getTypeArguments()))) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    public ProducedType withoutUnderlyingType() {
        ProducedType pt = new ProducedType();
        pt.setDeclaration(getDeclaration());
        pt.setQualifyingType(getQualifyingType());
        pt.setTypeArguments(getTypeArguments());
        return pt;
    }

    public boolean isRaw() {
        return isRaw;
    }

    public void setRaw(boolean isRaw) {
        this.isRaw = isRaw;
        // if we have a resolvedAliases cache, update it too
        if(resolvedAliases != null && resolvedAliases != this)
            resolvedAliases.setRaw(isRaw);
    }
    
    public ProducedType resolveAliases() {
        // cache the resolved version
        if(resolvedAliases == null){
            // really compute it
            resolvedAliases = curriedResolveAliases();
            // mark it as resolved so it doesn't get resolved again
            resolvedAliases.resolvedAliases = resolvedAliases;
            if(resolvedAliases != this){
                // inherit whatever underlying type we had
                resolvedAliases.underlyingType = underlyingType;
                resolvedAliases.isRaw = isRaw;
            }
        }
        return resolvedAliases;
    	//return curriedResolveAliases();
    }
    
    private ProducedType curriedResolveAliases() {
    	TypeDeclaration d = getDeclaration();
    	if (d instanceof UnionType) {
    		List<ProducedType> list = new ArrayList<ProducedType>();
    		for (ProducedType pt: d.getCaseTypes()) {
    			addToUnion(list, pt.resolveAliases());
    		}
    		UnionType ut = new UnionType(d.getUnit());
    		ut.setCaseTypes(list);
    		return ut.getType();
    	}
    	if (d instanceof IntersectionType) {
    		List<ProducedType> list = new ArrayList<ProducedType>();
    		for (ProducedType pt: d.getSatisfiedTypes()) {
    			addToIntersection(list, pt.resolveAliases(), d.getUnit());
    		}
    		IntersectionType ut = new IntersectionType(d.getUnit());
    		ut.setSatisfiedTypes(list);
    		return ut.canonicalize().getType();
    	}
    	List<ProducedType> args = getTypeArgumentList();
    	List<ProducedType> aliasedArgs = new ArrayList<ProducedType>(args.size());
    	for (ProducedType arg: args) {
    		aliasedArgs.add(arg==null ? null : arg.resolveAliases());
    	}
    	ProducedType qt = getQualifyingType();
    	ProducedType aliasedQualifyingType = qt==null ? 
    			null : qt.resolveAliases();
    	if (d.isAlias()) {
    		ProducedType et = d.getExtendedType();
			if (et == null) {
    			return new UnknownType(d.getUnit()).getType();
    		}
    		return et.resolveAliases()
    				.substitute(arguments(d, aliasedQualifyingType, aliasedArgs));
    	}
    	else {
    		return d.getProducedType(aliasedQualifyingType, aliasedArgs);
    	}
    }
    
}
