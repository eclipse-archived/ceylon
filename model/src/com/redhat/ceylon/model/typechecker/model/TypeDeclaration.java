package com.redhat.ceylon.model.typechecker.model;

import static com.redhat.ceylon.model.typechecker.model.Util.getSignature;
import static com.redhat.ceylon.model.typechecker.model.Util.getTypeArgumentMap;
import static com.redhat.ceylon.model.typechecker.model.Util.hasMatchingSignature;
import static com.redhat.ceylon.model.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.model.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.model.typechecker.model.Util.isResolvable;
import static com.redhat.ceylon.model.typechecker.model.Util.strictlyBetterMatch;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class TypeDeclaration extends Declaration 
        implements ImportableScope, Generic, Cloneable {

    private ProducedType extendedType;
    private List<ProducedType> satisfiedTypes = 
            needsSatisfiedTypes() ? 
                    new ArrayList<ProducedType>(3) : 
                    Collections.<ProducedType>emptyList();
    private List<ProducedType> caseTypes = null;
    private List<TypeParameter> typeParameters = emptyList();
    private ProducedType selfType;
    private List<ProducedType> brokenSupertypes = null; // delayed allocation
    private boolean inconsistentType;
    private boolean dynamic;
	private boolean sealed;

	/** 
	 * true if the type arguments of this type are not 
	 * available at runtime 
	 */
	public boolean isErasedTypeArguments() {
	    return false;
	}
	
	public boolean isSealed() {
	    return sealed;
    }
	
	public void setSealed(boolean sealed) {
	    this.sealed = sealed;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public boolean isInconsistentType() {
        return inconsistentType;
    }
    
    protected boolean needsSatisfiedTypes() {
        // NothingType doesn't need any so we save allocation
        return true;
    }

    public void setInconsistentType(boolean inconsistentType) {
        this.inconsistentType = inconsistentType;
    }
    
    public boolean isAbstract() {
        return true;
    }

    @Override
    protected TypeDeclaration clone() {
        try {
            return (TypeDeclaration) super.clone();
        } 
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isParameterized() {
        return !typeParameters.isEmpty();
    }

    public boolean isSelfType() {
    	return false;
    }
    
    public boolean isFinal() {
    	return false;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }
    
    /**
     * The class extended by a class, the type aliased
     * by a class or interface alias, or the class Anything
     * for any other type.
     */
    @Deprecated
    public ClassOrInterface getExtendedTypeDeclaration() {
        ProducedType et = getExtendedType();
		if (et==null) {
		    return null;
		}
		else {
		    TypeDeclaration etd = et.getDeclaration();
		    if (etd instanceof Constructor) {
		        //some classes directly extend a constructor
		        //of their superclass
		        return etd.getExtendedTypeDeclaration();
		    }
		    else if (etd instanceof ClassOrInterface) {
		        return (ClassOrInterface) etd;
		    }
		    else {
		        //plain type aliases "extend" their aliased
		        //type (yew!)
		        return null;
		    }
		}
    }

    public ProducedType getExtendedType() {
        return extendedType;
    }

    public void setExtendedType(ProducedType extendedType) {
        this.extendedType = extendedType;
    }

    @Deprecated
    public List<TypeDeclaration> getSatisfiedTypeDeclarations() {
        List<ProducedType> sts = getSatisfiedTypes();
        List<TypeDeclaration> list = 
                new ArrayList<TypeDeclaration>
                    (sts.size());
        for (int i=0, l=sts.size(); i<l; i++) {
            ProducedType pt = sts.get(i);
            list.add(pt==null ? null : pt.getDeclaration());
        }
        return list;
    }
    
    public List<ProducedType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    public void setSatisfiedTypes(List<ProducedType> satisfiedTypes) {
        this.satisfiedTypes = satisfiedTypes;
    }

    @Deprecated
    public List<TypeDeclaration> getCaseTypeDeclarations() {
        List<ProducedType> cts = getCaseTypes();
        if (cts==null) {
            return null;
        }
        else {
            List<TypeDeclaration> list = 
                    new ArrayList<TypeDeclaration>
                        (cts.size());
            for (int i=0, l=cts.size(); i<l; i++) {
                ProducedType pt = cts.get(i);
                list.add(pt==null ? null : pt.getDeclaration());
            }
            return list;
        }
    }

    public List<ProducedType> getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(List<ProducedType> caseTypes) {
        this.caseTypes = caseTypes;
    }
    
    public List<ProducedType> getBrokenSupertypes() {
        return brokenSupertypes == null ? 
                Collections.<ProducedType>emptyList() : 
                brokenSupertypes;
    }
    
    public void addBrokenSupertype(ProducedType type) {
        if (brokenSupertypes == null) {
            brokenSupertypes = 
                    new ArrayList<ProducedType>(1);
        }
        brokenSupertypes.add(type);
    }
    
    @Override
    public ProducedReference getProducedReference(
            ProducedType pt,
            List<ProducedType> typeArguments) {
        return getProducedType(pt, typeArguments);
    }

    @Override
    public final ProducedType getReference() {
        return getType();
    }

    /**
     * Get a produced type for this declaration by
     * binding explicit or inferred type arguments
     * and type arguments of the type of which this
     * declaration is a member, in the case that this
     * is a nested type.
     *
     * @param qualifyingType the qualifying produced
     *                       type or null if this is 
     *                       not a nested type dec
     * @param typeArguments arguments to the type
     *                      parameters of this 
     *                      declaration
     */
    public ProducedType getProducedType(
            ProducedType qualifyingType,
            List<ProducedType> typeArguments) {
    	if (qualifyingType!=null && 
                qualifyingType.isNothing()) {
        	return qualifyingType;
        }
    	else {
            ProducedType pt = new ProducedType();
            pt.setDeclaration(this);
            pt.setQualifyingType(qualifyingType);
            pt.setTypeArguments(getTypeArgumentMap(this, 
                    qualifyingType, typeArguments));
            return pt;
    	}
    }
    
    /**
     * The type of the declaration as seen from
     * within the body of the declaration itself.
     * <p/>
     * Note that for certain special types which
     * we happen to know don't have type arguments,
     * we use this as a convenience method to
     * quickly get a produced type for use outside
     * the body of the declaration, but this is not
     * really correct!
     */
    public ProducedType getType() {
        ProducedType type = new ProducedType();
        type.setQualifyingType(getMemberContainerType());
        type.setDeclaration(this);
        type.setTypeArguments(getTypeParametersAsArguments());
        return type;
    }

    private List<Declaration> getMembers(String name, 
            List<TypeDeclaration> visited) {
        if (visited.contains(this)) {
            return Collections.emptyList();
        }
        else {
            visited.add(this);
            List<Declaration> members = 
                    new ArrayList<Declaration>();
            for (Declaration d: getMembers()) {
                if (d.getName()!=null && 
                        d.getName().equals(name)) {
                    members.add(d);
                }
            }
            if (members.isEmpty()) {
                members.addAll(getInheritedMembers(name, visited));
            }
            return members;
        }
    }
    
    /**
     * Get all members inherited by this type declaration
     * with the given name. Do not include members declared
     * directly by this type. Do not include declarations 
     * refined by a supertype.
     * 
     * @deprecated This does not handle Java's inheritance
     *             model where overloads can be inherited 
     *             from different supertypes ... I think we  
     *             can remove this now and use getRefinedMember()
     */
    public List<Declaration> getInheritedMembers(String name) {
        return getInheritedMembers(name, 
                new ArrayList<TypeDeclaration>());
    }
    
    //identity containment
    private static <T> boolean contains(Iterable<T> iter, T object) {
        for (Object elem: iter) {
            if (elem==object) {
                return true;
            }
        }
        return false;
    }
    
    private List<Declaration> getInheritedMembers(String name, 
            List<TypeDeclaration> visited) {
        List<Declaration> members = 
                new ArrayList<Declaration>();
        for (ProducedType st: getSatisfiedTypes()) {
            //if ( !(t instanceof TypeParameter) ) { //don't look for members in a type parameter with a self-referential lower bound
            for (Declaration member: 
                    st.getDeclaration()
                        .getMembers(name, visited)) {
                if (member.isShared() && 
                        isResolvable(member)) {
                    if (!contains(members, member)) {
                    	members.add(member);
                    }
                }
            }
            //}
        }
        ProducedType et = getExtendedType();
        if (et!=null) {
            for (Declaration member: 
                    et.getDeclaration()
                        .getMembers(name, visited)) {
                if (member.isShared() && 
                        isResolvable(member)) {
                    if (!contains(members, member)) {
                    	members.add(member);
                    }
                }
            }
        }
        return members;
    }
    
    /**
     * Is the given declaration a direct or inherited
     * member of this type?
     */
    public boolean isMember(Declaration dec) {
        return isMember(dec, 
                new ArrayList<TypeDeclaration>());
    }
    
    private boolean isMember(Declaration dec, 
            List<TypeDeclaration> visited) {
        if (visited.contains(this)) {
            return false;
        }
        visited.add(this);
        for (Declaration member: getMembers()) {
            if (dec.equals(member)) {
                return true;
            }
        }
        for (ProducedType t: getSatisfiedTypes()) {
            if (t.getDeclaration().isMember(dec, visited)) {
                return true;
            }
        }
        ProducedType et = getExtendedType();
        if (et!=null) {
            if (et.getDeclaration()
                    .isMember(dec, visited)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Does the given declaration inherit the given type?
     */
    public boolean inherits(TypeDeclaration dec) {
        //TODO: optimize this to avoid walking the
        //      same supertypes multiple times
        for (ProducedType st: getSatisfiedTypes()) {
            if (st.getDeclaration().inherits(dec)) {
                return true;
            }
        }
        ProducedType et = getExtendedType();
        if (et!=null && 
                et.getDeclaration().inherits(dec)) {
            return true;
        }
        return false;
    }
    
    /**
     * Return the least-refined (i.e. the non-actual member)
     * with the given name, by reversing the usual search
     * order and searching supertypes first.
     */
    public Declaration getRefinedMember(String name, 
            List<ProducedType> signature, boolean ellipsis) {
        return getRefinedMember(name, signature, ellipsis,
                        new HashSet<TypeDeclaration>());
    }

    protected Declaration getRefinedMember(String name, 
            List<ProducedType> signature, boolean ellipsis, 
            Set<TypeDeclaration> visited) {
        if (!visited.add(this)) {
            return null;
        }
        else {
            Declaration result = null;
            ProducedType et = getExtendedType();
            if (et!=null) {
                Declaration ed = 
                        et.getDeclaration()
                            .getRefinedMember(name, 
                                    signature, ellipsis, 
                                    visited);
                if (isBetterRefinement(signature, 
                        ellipsis, result, ed)) {
                    result = ed;
                }
            }
            for (ProducedType st: getSatisfiedTypes()) {
                Declaration sd = 
                        st.getDeclaration()
                            .getRefinedMember(name, 
                                    signature, ellipsis, 
                                    visited);
                if (isBetterRefinement(signature, 
                        ellipsis, result, sd)) {
                    result = sd;
                }
            }
            Declaration dd = 
                    getDirectMember(name, signature, ellipsis);
            if (isBetterRefinement(signature, 
                    ellipsis, result, dd)) {
                result = dd;
            }
            return result;
        }
    }

    public boolean isBetterRefinement(
            List<ProducedType> signature, boolean ellipsis, 
            Declaration result, Declaration candidate) {
        if (candidate==null ||
                candidate.isActual() /*&& 
                !candidate.getNameAsString()
                    .equals("ceylon.language::Object")*/ || 
                !candidate.isShared()) {
            return false;
        }
        if (result==null) {
            return signature!=null == 
                    candidate instanceof Functional;
        }
        if (!(result instanceof Functional)) {
            return signature!=null;
        }
        if (!(candidate instanceof Functional)) {
            return signature==null;
        }
        if (signature==null) {
            throw new RuntimeException("missing signature");
        }
        if (candidate.isAbstraction() && 
                !result.isAbstraction()) {
            return false;
        }
        if (!candidate.isAbstraction() && 
                result.isAbstraction()) {
            return true;
        }
        if (hasMatchingSignature(candidate, signature, ellipsis)) {
            return !hasMatchingSignature(result, signature, ellipsis) || 
                    strictlyBetterMatch(candidate, result);
        }
        return false; //asymmetric!!
    }
    
    /**
     * Get the most-refined member with the given name,
     * searching this type first, taking aliases into
     * account, followed by supertypes. We're looking
     * for shared members.
     */
    public Declaration getMember(
            String name, 
            Unit unit, 
            List<ProducedType> signature, 
            boolean variadic) {
        //TODO: does not handle aliased members of supertypes
        Declaration dec = 
                unit.getImportedDeclaration(this, name, 
                        signature, variadic);
        if (dec==null) {
            return getMemberInternal(name, signature, variadic)
                    .getMember();
        }
        else {
            return dec;
        }
    }

    /**
     * Is the most-refined member with the given name,
     * searching this type first, taking aliases into
     * account, followed by supertypes, ambiguous,
     * because we could not construct a principal
     * instantiation for an intersection?
     * TODO: this information should really be encoded 
     *       into the return value of getMember() but
     *       I'm leaving it like this for now to avoid
     *       breaking the backends
     */
    public boolean isMemberAmbiguous(
            String name, 
            Unit unit, 
            List<ProducedType> signature, 
            boolean variadic) {
        //TODO: does not handle aliased members of supertypes
        Declaration dec = 
                unit.getImportedDeclaration(this, name, 
                        signature, variadic);
        if (dec==null) {
            return getMemberInternal(name, signature, variadic)
                    .isAmbiguous();
        }
        else {
            return false;
        }
    }

    /**
     * Get the most-refined member with the given name,
     * searching this type first, followed by supertypes.
     * We're looking for shared members.
     */
    @Override
    public Declaration getMember(
            String name, 
            List<ProducedType> signature, 
            boolean variadic) {
        return getMemberInternal(name, signature, variadic)
                .getMember();
    }

    private SupertypeDeclaration getMemberInternal(
            String name,
            List<ProducedType> signature, boolean variadic) {
        //first search for the member in the local
        //scope, including non-shared declarations
        Declaration dec = 
                getDirectMember(name, 
                        signature, variadic);
        if (dec!=null && dec.isShared()) {
            //if it's shared, it's what we're 
            //looking for, return it
            //TODO: should also return it if we're 
            //      calling from local scope!
            if (signature!=null && dec.isAbstraction()){
                //look for a supertype declaration that matches 
                //the given signature better
                SupertypeDeclaration sd = 
                        getSupertypeDeclaration(name, 
                                signature, variadic);
                Declaration sm = sd.getMember();
                if (sm!=null && !sm.isAbstraction()) {
                    return sd;
                }
            }
            return new SupertypeDeclaration(dec, false);
        }
        else {
            //now look for inherited shared declarations
            SupertypeDeclaration sd = 
                    getSupertypeDeclaration(name, 
                            signature, variadic);
            if (sd.getMember()!=null || sd.isAmbiguous()) {
                return sd;
            }
        }
        //finally return the non-shared member we
        //found earlier, so that the caller can give
        //a nice error message
        return new SupertypeDeclaration(dec, false);
    }
    
    /**
     * Get the parameter or most-refined member with the 
     * given name, searching this type first, followed by 
     * supertypes. Return un-shared members declared by
     * this type.
     */
    @Override
    protected Declaration getMemberOrParameter(
            String name, 
            List<ProducedType> signature, 
            boolean variadic) {
        //first search for the member or parameter 
        //in the local scope, including non-shared 
        //declarations
        Declaration dec = 
                getDirectMember(name, 
                        signature, variadic);
        if (dec!=null) {
            if (signature!=null && 
                    dec.isAbstraction()) {
                // look for a supertype declaration that matches 
                // the given signature better
                Declaration supertype = 
                        getSupertypeDeclaration(name, 
                                signature, variadic)
                                .getMember();
                if (supertype!=null && 
                        !supertype.isAbstraction()) {
                    return supertype;
                }
            }
            return dec;
        }
        else {
            //now look for inherited shared declarations
            return getSupertypeDeclaration(name, 
                    signature, variadic)
                    .getMember();
        }
    }

    /**
     * Is the given declaration inherited from
     * a supertype of this type or an outer
     * type?
     * 
     * @return true if it is
     */
    @Override
    public boolean isInherited(Declaration member) {
        if (member.getContainer().equals(this)) {
            return false;
        }
        else if (isInheritedFromSupertype(member)) {
            return true;
        }
        else if (getContainer()!=null) {
            return getContainer()
                    .isInherited(member);
        }
        else {
            return false;
        }
    }

    /**
     * Get the containing type which inherits the given declaration.
     * 
     * @return null if the declaration is not inherited!!
     */
    @Override
    public TypeDeclaration getInheritingDeclaration(
            Declaration member) {
        if (member.getContainer().equals(this)) {
            return null;
        }
        else if (!(this instanceof Constructor) && 
                isInheritedFromSupertype(member)) {
            return this;
        }
        else if (getContainer()!=null) {
            return getContainer()
                    .getInheritingDeclaration(member);
        }
        else {
            return null;
        }
    }

    public boolean isInheritedFromSupertype(
            final Declaration member) {
        final List<ProducedType> signature = 
                getSignature(member);
        class Criteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                if (type.equals(TypeDeclaration.this)) {
                    return false;
                }
                else {
                    Declaration dm = 
                            type.getDirectMember(
                                    member.getName(), 
                                    signature, false);
                    return dm!=null && dm.equals(member);
                }
            }
            @Override
            public boolean isMemberLookup() {
            	return false;
            }
        };
        return getType()
                .getSupertype(new Criteria())!=null;
    }
    
    static class SupertypeDeclaration {
        private Declaration member;
        private boolean ambiguous;
        SupertypeDeclaration(Declaration declaration, 
                boolean ambiguous) {
            this.member = declaration;
            this.ambiguous = ambiguous;
        }
        private boolean isAmbiguous() {
            return ambiguous;
        }
        private Declaration getMember() {
            return member;
        }
    }
    
    /**
     * Get the supertype which defines the most-refined
     * member with the given name.
     */
    SupertypeDeclaration getSupertypeDeclaration(
            final String name, 
            final List<ProducedType> signature, 
            final boolean variadic) {
        class ExactCriteria 
                implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                // do not look in ourselves
                if (type == TypeDeclaration.this) {
                    return false;
                }
                Declaration dm = 
                        type.getDirectMember(name, 
                                signature, variadic);
                if (dm!=null && 
                        dm.isShared() && 
                        isResolvable(dm)) {
                    // only accept abstractions if we don't have a signature
                    return !dm.isAbstraction() || 
                            signature == null;
                }
                else {
                    return false;
                }
            }
            @Override
            public boolean isMemberLookup() {
            	return true;
            }
        };
        class LooseCriteria 
                implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                // do not look in ourselves
                if (type == TypeDeclaration.this) {
                    return false;
                }
                Declaration dm = 
                        type.getDirectMember(name, null, false);
                if (dm!=null && 
                        dm.isShared() && 
                        isResolvable(dm)) {
                    // only accept abstractions
                    return dm.isAbstraction();
                }
                else {
                    return false;
                }
            }
            @Override
            public boolean isMemberLookup() {
                return true;
            }
        };
        //this works by finding the most-specialized supertype
        //that defines the member
        ProducedType type = getType();
        ProducedType st = 
                type.getSupertype(new ExactCriteria());
        if (st == null) {
            //try again, ignoring the given signature
            st = type.getSupertype(new LooseCriteria());
        }
        if (st == null) {
            //no such member
            return new SupertypeDeclaration(null, false);
        }
        else if (st.isUnknown()) {
            //we're dealing with an ambiguous member of an 
            //intersection type
            //TODO: this is pretty fragile - it depends upon
            //      the fact that getSupertype() just happens
            //      to return an UnknownType instead of null
            //      in this case
            return new SupertypeDeclaration(null, true);
        }
        else {
            //we got exactly one uniquely-defined member
            Declaration member = 
                    st.getDeclaration()
                        .getDirectMember(name, 
                                signature, variadic);
            return new SupertypeDeclaration(member, false);
        }
    }
    
    /**
     * Is this a class or interface alias? 
     */
    public boolean isAlias() {
        return false;
    }

    public void setSelfType(ProducedType selfType) {
        this.selfType = selfType;
    }

    public ProducedType getSelfType() {
        return selfType;
    }
    
    public Map<String,DeclarationWithProximity> 
    getImportableDeclarations(Unit unit, String startingWith, 
            List<Import> imports, int proximity) {
        //TODO: fix copy/paste from below!
        Map<String,DeclarationWithProximity> result = 
                new TreeMap<String,DeclarationWithProximity>();
        for (Declaration dec: getMembers()) {
            if (isResolvable(dec) && 
                    dec.isShared() && 
            		!isOverloadedVersion(dec) &&
                    isNameMatching(startingWith, dec) ) {
                boolean already = false;
                for (Import i: imports) {
                    if (i.getDeclaration().equals(dec)) {
                        already = true;
                        break;
                    }
                }
                if (!already) {
                    result.put(dec.getName(unit), 
                            new DeclarationWithProximity(dec, 
                                    proximity));
                }
            }
        }
        return result;
    }
    
    @Override
    public Map<String,DeclarationWithProximity> 
    getMatchingDeclarations(Unit unit, String startingWith, 
            int proximity) {
        Map<String,DeclarationWithProximity> result = 
                super.getMatchingDeclarations(unit, 
                        startingWith, proximity);
        //Inherited declarations hide outer and imported declarations
        result.putAll(getMatchingMemberDeclarations(unit, 
                null, startingWith, proximity));
        //Local declarations always hide inherited declarations, even if non-shared
        for (Declaration dec: getMembers()) {
            if (isResolvable(dec) && 
                    !isOverloadedVersion(dec) &&
            		isNameMatching(startingWith, dec)) {
                result.put(dec.getName(unit), 
                		new DeclarationWithProximity(dec, 
                		        proximity));
            }
        }
        return result;
    }

    public Map<String,DeclarationWithProximity> 
    getMatchingMemberDeclarations(Unit unit, Scope scope, 
            String startingWith, int proximity) {
        Map<String,DeclarationWithProximity> result = 
                new TreeMap<String,DeclarationWithProximity>();
        for (ProducedType st: getSatisfiedTypes()) {
            mergeMembers(result, 
                    st.getDeclaration()
                        .getMatchingMemberDeclarations(unit, 
                                scope, startingWith, 
                                proximity+1));
        }
        ProducedType et = 
                getExtendedType();
        if (et!=null) {
            mergeMembers(result, 
                    et.getDeclaration()
                        .getMatchingMemberDeclarations(unit, 
                                scope, startingWith, 
                                proximity+1));
        }
        for (Declaration member: getMembers()) {
            if (isResolvable(member) && 
                    !isOverloadedVersion(member) &&
                    isNameMatching(startingWith, member)) {
                if( member.isShared() || 
                        Util.contains(member.getScope(), scope) ) {
                    result.put(member.getName(unit), 
                            new DeclarationWithProximity(
                                    member, proximity));
                }
            }
        }
        //premature optimization so that we don't have to 
        //call d.getName(unit) on *every* member
        result.putAll(unit.getMatchingImportedDeclarations(
                this, startingWith, proximity));
        return result;
    }

    private void mergeMembers(
            Map<String,DeclarationWithProximity> result,
            Map<String,DeclarationWithProximity> etm) {
        for (Map.Entry<String,DeclarationWithProximity> e: 
                etm.entrySet()) {
            String name = e.getKey();
            DeclarationWithProximity current = 
                    e.getValue();
            DeclarationWithProximity existing = 
                    result.get(name);
            if (existing==null || 
                    !existing.getDeclaration()
                        .refines(current.getDeclaration())) {
                result.put(name, current);
            }
        }
    }
    
    public final List<TypeDeclaration> getSupertypeDeclarations() {
        ArrayList<TypeDeclaration> results = 
                new ArrayList<TypeDeclaration>(
                        getSatisfiedTypes().size()+2);
        results.add(unit.getAnythingDeclaration());
        collectSupertypeDeclarations(results);
        return results;
    }
    
    abstract void collectSupertypeDeclarations(
            List<TypeDeclaration> results);
    
    /**
     * Clears the ProducedType supertype caches for that declaration. Does nothing
     * for Union/Intersection types since they are not cached. Only does something
     * for ClassOrInterface, TypeAlias, TypeParameter.
     */
    public void clearProducedTypeCache() {
        // do nothing, work in subclasses
    }

}
