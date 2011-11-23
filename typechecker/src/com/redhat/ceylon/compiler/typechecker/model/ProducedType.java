package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.addToUnion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.addToIntersection;
import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

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
        if (getDeclaration() instanceof BottomType) {
            return type.getDeclaration() instanceof BottomType;
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
                            if (c.isExactly(oc)) {
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
                return st.isExactly(type);
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
                            if (c.isExactly(oc)) {
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
                return st.isExactly(type);
            }
            else {
                return false;
            }
        }
        else if (type.getDeclaration() instanceof UnionType) {
            List<ProducedType> otherCases = type.getCaseTypes();
            if (otherCases.size()==1) {
                ProducedType st = otherCases.get(0);
                return this.isExactly(st);
            }
            else {
                return false;
            }
        }
        else if (type.getDeclaration() instanceof IntersectionType) {
            List<ProducedType> otherTypes = type.getSatisfiedTypes();
            if (otherTypes.size()==1) {
                ProducedType st = otherTypes.get(0);
                return this.isExactly(st);
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
                ProducedType qt = getQualifyingType();
                ProducedType tqt = type.getQualifyingType();
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
                        if ( !qts.isExactly(tqts) ) {
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
                    else if (!arg.isExactly(otherArg)) {
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
        return isSubtypeOf(type, null);
    }
    
    /**
     * Is this type a subtype of the given type? Ignore
     * a certain self type constraint.
     */
    public boolean isSubtypeOf(ProducedType type, TypeDeclaration selfTypeToIgnore) {
        if (getDeclaration() instanceof BottomType) {
            return true;
        }
        else if (type.getDeclaration() instanceof BottomType) {
            return false;
        }
        else if (getDeclaration() instanceof UnionType) {
            for (ProducedType ct: getInternalCaseTypes()) {
                if (ct==null || !ct.isSubtypeOf(type, selfTypeToIgnore)) {
                    return false;
                }
            }
            return true;
        }
        else if (type.getDeclaration() instanceof UnionType) {
            for (ProducedType ct: type.getInternalCaseTypes()) {
                if (ct!=null && isSubtypeOf(ct, selfTypeToIgnore)) {
                    return true;
                }
            }
            return false;
        }
        else if (type.getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: type.getInternalSatisfiedTypes()) {
                if (ct!=null && !isSubtypeOf(ct, selfTypeToIgnore)) {
                    return false;
                }
            }
            return true;
        }
        else if (getDeclaration() instanceof IntersectionType) {
            for (ProducedType ct: getInternalSatisfiedTypes()) {
                if (ct==null || ct.isSubtypeOf(type, selfTypeToIgnore)) {
                    return true;
                }
            }
            return false;
        }
        else {
            ProducedType st = getSupertype(type.getDeclaration(), selfTypeToIgnore);
            if (st==null) {
                return false;
            }
            else {
                ProducedType stqt = st.getQualifyingType();
                ProducedType tqt = type.getQualifyingType();
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
                        if (!arg.isExactly(otherArg)) {
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
        if (getDeclaration().equals(ci)) {
            return new BottomType(getDeclaration().getUnit()).getType();
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
        ProducedType pt = new ProducedType();
        pt.setDeclaration(member);
        pt.setQualifyingType(declaringType);
        Map<TypeParameter, ProducedType> map = arguments(member, declaringType, typeArguments);
        //map.putAll(sub(map));
        pt.setTypeArguments(map);
        return pt;
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
        if ( isWellDefined() && Util.addToSupertypes(list, this) ) {
            ProducedType extendedType = getExtendedType();
            if (extendedType!=null) {
                extendedType.getSupertypes(list);
            }
            for (ProducedType dst: getSatisfiedTypes()) {
                dst.getSupertypes(list);
            }
            ProducedType selfType = getSelfType();
            if (selfType!=null) {
                if (!(selfType.getDeclaration() instanceof TypeParameter)) { //TODO: is this really correct???
                    selfType.getSupertypes(list);
                }
            }
            List<ProducedType> caseTypes = getCaseTypes();
            if (caseTypes!=null /*&& !getDeclaration().getCaseTypes().isEmpty()*/) {
                for (ProducedType t: caseTypes) {
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
        return list;
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
    public ProducedType getSupertype(TypeDeclaration dec) {
        return getSupertype(dec, null);
    }
    
    /**
     * Given a type declaration, return a produced type of 
     * which this type is an invariant subtype. Ignore a 
     * given self type constraint. 
     * 
     * @param dec a type declaration
     * @param selfTypeToIgnore a self type to ignore when 
     *        searching for supertypes
     * 
     * @return a produced type of the given type declaration
     *         which is a supertype of this type, or null if
     *         there is no such supertype
     */
    private ProducedType getSupertype(final TypeDeclaration dec, 
            TypeDeclaration selfTypeToIgnore) {
        Criteria c = new Criteria() {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                return type.equals(dec);
            }
        };
        return getSupertype(c, new ArrayList<ProducedType>(), selfTypeToIgnore);
    }
    
    /**
     * Given a predicate, return a produced type for a 
     * declaration satisfying the predicate, of which 
     * this type is an invariant subtype. 
     */
    ProducedType getSupertype(Criteria c) {
        return getSupertype(c, new ArrayList<ProducedType>(), null);
    }
    
    static interface Criteria {
        boolean satisfies(TypeDeclaration type);
    }
    
    /**
     * Search for the most-specialized supertype 
     * satisfying the given predicate. 
     */
    private ProducedType getSupertype(final Criteria c, List<ProducedType> list, 
            final TypeDeclaration ignoringSelfType) {
        if (c.satisfies(getDeclaration())) {
            return qualifiedByDeclaringType();
        }
        if ( isWellDefined() && Util.addToSupertypes(list, this) ) {
            //search for the most-specific supertype 
            //for the given declaration
            ProducedType result = null;
            ProducedType extendedType = getInternalExtendedType();
            if (extendedType!=null) {
                ProducedType possibleResult = extendedType.getSupertype(c, list, 
                                ignoringSelfType);
                if (possibleResult!=null) {
                    result = possibleResult;
                }
            }
            for (ProducedType dst: getInternalSatisfiedTypes()) {
                ProducedType possibleResult = dst.getSupertype(c, list, 
                                ignoringSelfType);
                if (possibleResult!=null && (result==null || 
                        possibleResult.isSubtypeOf(result, ignoringSelfType))) {
                    result = possibleResult;
                }
            }
            if (!getDeclaration().equals(ignoringSelfType)) {
                ProducedType selfType = getInternalSelfType();
                if (selfType!=null) {
                    ProducedType possibleResult = selfType.getSupertype(c, list, 
                                ignoringSelfType);
                    if (possibleResult!=null && (result==null || 
                            possibleResult.isSubtypeOf(result, ignoringSelfType))) {
                        result = possibleResult;
                    }
                }
            }
            final List<ProducedType> caseTypes = getInternalCaseTypes();
            if (caseTypes!=null && !caseTypes.isEmpty()) {
                //first find a common superclass or superinterface 
                //declaration that satisfies the criteria, ignoring
                //type arguments for now
                Criteria c2 = new Criteria() {
                    @Override
                    public boolean satisfies(TypeDeclaration type) {
                        if ( c.satisfies(type) ) {
                            for (ProducedType ct: caseTypes) {
                                if (ct.getSupertype(type, ignoringSelfType)==null) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                };
                ProducedType stc = caseTypes.get(0).getSupertype(c2, list, 
                                ignoringSelfType);
                if (stc!=null) {
                    //we found the declaration, now try to construct a 
                    //produced type that is a true common supertype
                    ProducedType candidateResult = getCommonSupertype(caseTypes, 
                            stc.getDeclaration(), ignoringSelfType);
                    if (candidateResult!=null && (result==null || 
                            candidateResult.isSubtypeOf(result, ignoringSelfType))) {
                        result = candidateResult;
                    }
                }
            }
            return result;
        }
        else {
            return null;
        }
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
            TypeDeclaration dec, final TypeDeclaration selfTypeToIgnore) {
        //now try to construct a common produced
        //type that is a common supertype by taking
        //the type args and unioning them
        List<ProducedType> args = new ArrayList<ProducedType>();
        for (TypeParameter tp: dec.getTypeParameters()) {
            List<ProducedType> list2 = new ArrayList<ProducedType>();
            ProducedType result;
            if (tp.isContravariant()) { 
                for (ProducedType pt: caseTypes) {
                    ProducedType st = pt.getSupertype(dec, selfTypeToIgnore);
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
                    ProducedType st = pt.getSupertype(dec, selfTypeToIgnore);
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
        for (int i=0; i<args.size(); i++) {
            TypeParameter tp = dec.getTypeParameters().get(i);
            for (ProducedType ub: tp.getSatisfiedTypes()) {
                if (!args.get(i).isSubtypeOf(ub)) {
                    return null;
                }
            }
        }
        //recurse to the qualifying type
        ProducedType outerType;
        if (dec.isMember()) {
            TypeDeclaration outer = (TypeDeclaration) dec.getContainer();
            List<ProducedType> list = new ArrayList<ProducedType>();
            for (ProducedType pt: caseTypes) {
                ProducedType st = pt.getQualifyingType().getSupertype(outer, null);
                list.add(st);
            }
            outerType = getCommonSupertype(list, outer, null);
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

    private ProducedType getInternalSelfType() {
        ProducedType selfType = getDeclaration().getSelfType();
        return selfType==null?null:selfType.substituteInternal(getTypeArguments());
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

    private ProducedType getSelfType() {
        ProducedType selfType = getDeclaration().getSelfType();
        return selfType==null?null:selfType.substitute(getTypeArguments());
    }

    private List<ProducedType> getSatisfiedTypes() {
        List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
        for (ProducedType st: getDeclaration().getSatisfiedTypes()) {
            satisfiedTypes.add(st.substitute(getTypeArguments()));
        }
        return satisfiedTypes;
    }

    private ProducedType getExtendedType() {
        ProducedType extendedType = getDeclaration().getExtendedType();
        return extendedType==null?null:extendedType.substitute(getTypeArguments());
    }

    private List<ProducedType> getCaseTypes() {
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
        return getProducedTypeName(true);
    }

    public String getProducedTypeName(boolean abbreviate) {
        if (getDeclaration()==null) {
            //unknown type
            return null;
        }
        if (abbreviate && getDeclaration() instanceof UnionType) {
            UnionType ut = (UnionType) getDeclaration();
            if (ut.getCaseTypes().size()==2) {
                Unit unit = getDeclaration().getUnit();
                if (Util.isElementOfUnion(ut, unit.getNothingDeclaration())) {
                    return unit.getDefiniteType(this)
                            .getProducedTypeName() + "?";
                }
                if (Util.isElementOfUnion(ut, unit.getEmptyDeclaration()) &&
                        Util.isElementOfUnion(ut, unit.getSequenceDeclaration())) {
                    return unit.getElementType(this)
                            .getProducedTypeName() + "[]";
                }
            }
        }
        String producedTypeName = "";
        if (getDeclaration().isMember()) {
            producedTypeName += getQualifyingType().getProducedTypeName(abbreviate);
            producedTypeName += ".";
        }
        producedTypeName += getDeclaration().getName();
        if (!getTypeArgumentList().isEmpty()) {
            producedTypeName += "<";
            for (ProducedType t : getTypeArgumentList()) {
                if (t==null) {
                    producedTypeName += "unknown,";
                }
                else {
                    producedTypeName += t.getProducedTypeName(abbreviate) + ",";
                }
            }
            producedTypeName += ">";
            producedTypeName = producedTypeName.replace(",>", ">");
        }
        return producedTypeName;
    }

    public String getProducedTypeQualifiedName() {
        if (getDeclaration()==null) {
            //unknown type
            return null;
        }
        String producedTypeName = "";
        if (getDeclaration().isMember()) {
            producedTypeName += getQualifyingType().getProducedTypeQualifiedName();
            producedTypeName += ".";
        }
        producedTypeName += getDeclaration().getQualifiedNameString();
        if (!getTypeArgumentList().isEmpty()) {
            producedTypeName += "<";
            for (ProducedType t : getTypeArgumentList()) {
                if (t==null) {
                    producedTypeName += "?,";
                }
                else {
                    producedTypeName += t.getProducedTypeQualifiedName() + ",";
                }
            }
            producedTypeName += ">";
            producedTypeName = producedTypeName.replace(",>", ">");
        }
        return producedTypeName;
    }

}
