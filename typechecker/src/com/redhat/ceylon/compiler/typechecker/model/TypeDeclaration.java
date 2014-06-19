package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.getTypeArgumentMap;
import static com.redhat.ceylon.compiler.typechecker.model.Util.getSignature;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isOverloadedVersion;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class TypeDeclaration extends Declaration 
        implements ImportableScope, Generic, Cloneable {

    private ProducedType extendedType;
    private List<ProducedType> satisfiedTypes = needsSatisfiedTypes() ? 
            new ArrayList<ProducedType>(3) : 
                Collections.<ProducedType>emptyList();
    private List<ProducedType> caseTypes = null;
    private List<TypeParameter> typeParameters = emptyList();
    private ProducedType selfType;
    // delayed allocation
    private List<ProducedType> brokenSupertypes = null;
    private boolean inconsistentType;
    private boolean dynamic;
	private boolean sealed;
    
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

    public ClassOrInterface getExtendedTypeDeclaration() {
        ProducedType et = getExtendedType();
		if (et==null || 
        		!(et.getDeclaration() instanceof ClassOrInterface)) {
            return null;
        }
        else {
            return (ClassOrInterface) et.getDeclaration();
        }
    }

    public ProducedType getExtendedType() {
        return extendedType;
    }

    public void setExtendedType(ProducedType extendedType) {
        this.extendedType = extendedType;
    }

    public List<TypeDeclaration> getSatisfiedTypeDeclarations() {
        List<ProducedType> sts = getSatisfiedTypes();
        List<TypeDeclaration> list = new ArrayList<TypeDeclaration>(sts.size());
        for (ProducedType pt: sts) {
            list.add(pt==null?null:pt.getDeclaration());
        }
        return list;
    }

    public List<ProducedType> getSatisfiedTypes() {
        return satisfiedTypes;
    }

    public void setSatisfiedTypes(List<ProducedType> satisfiedTypes) {
        this.satisfiedTypes = satisfiedTypes;
    }

    public List<TypeDeclaration> getCaseTypeDeclarations() {
        List<ProducedType> caseTypes = getCaseTypes();
        if (caseTypes==null) {
            return null;
        }
        else {
            List<TypeDeclaration> list = new ArrayList<TypeDeclaration>(caseTypes.size());
            for (ProducedType pt: caseTypes) {
                list.add(pt==null?null:pt.getDeclaration());
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
        return brokenSupertypes == null ? Collections.<ProducedType>emptyList() : brokenSupertypes;
    }
    
    public void addBrokenSupertype(ProducedType type){
        if(brokenSupertypes == null)
            brokenSupertypes = new ArrayList<ProducedType>(1);
        brokenSupertypes.add(type);
    }
    
    @Override
    public ProducedReference getProducedReference(ProducedType pt,
            List<ProducedType> typeArguments) {
        return getProducedType(pt, typeArguments);
    }

    @Override
    public ProducedType getReference() {
    	ProducedType pt = new ProducedType();
        if (isMember()) {
            pt.setQualifyingType(((ClassOrInterface) getContainer()).getType());
        }
        pt.setDeclaration(this);
        pt.setTypeArguments(getTypeArgumentMap(this, pt.getQualifyingType(), 
        		Collections.<ProducedType>emptyList()));
        return pt;
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
    public ProducedType getProducedType(ProducedType qualifyingType,
            List<ProducedType> typeArguments) {
    	if (qualifyingType!=null && qualifyingType.isNothing()) {
    		return qualifyingType;
    	}
        ProducedType pt = new ProducedType();
        pt.setDeclaration(this);
        pt.setQualifyingType(qualifyingType);
        pt.setTypeArguments(getTypeArgumentMap(this, qualifyingType, typeArguments));
        return pt;
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
        if (isMember()) {
            type.setQualifyingType(((ClassOrInterface) getContainer()).getType());
        }
        type.setDeclaration(this);
        //each type parameter is its own argument
        List<TypeParameter> typeParameters = getTypeParameters();
        if (typeParameters.isEmpty()) {
            type.setTypeArguments(Collections.<TypeParameter,ProducedType>emptyMap());
        }
        else {
            Map<TypeParameter, ProducedType> map = 
            		new HashMap<TypeParameter, ProducedType>();
            for (TypeParameter p: typeParameters) {
                ProducedType pta = new ProducedType();
                pta.setDeclaration(p);
                map.put(p, pta);
            }
            type.setTypeArguments(map);
        }
        return type;
    }

    private List<Declaration> getMembers(String name, 
            List<TypeDeclaration> visited) {
        if (visited.contains(this)) {
            return Collections.emptyList();
        }
        else {
            visited.add(this);
            List<Declaration> members = new ArrayList<Declaration>();
            for (Declaration d: getMembers()) {
                if (d.getName()!=null && d.getName().equals(name)) {
                    members.add(d);
                }
            }
            if (members.isEmpty()) {
                members.addAll(getInheritedMembers(name));
            }
            return members;
        }
    }
    
    /**
     * Get all members inherited by this type declaration
     * with the given name. Do not include members declared
     * directly by this type. Do not include declarations 
     * refined by a supertype.
     */
    public List<Declaration> getInheritedMembers(String name) {
        return getInheritedMembers(name, new ArrayList<TypeDeclaration>());
    }
    
    private static <T> boolean contains(Iterable<T> iter, T object) {
        for (Object elem: iter) {
            if (elem==object) return true;
        }
        return false;
    }
    
    private List<Declaration> getInheritedMembers(String name, 
            List<TypeDeclaration> visited) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (TypeDeclaration t: getSatisfiedTypeDeclarations()) {
            //if ( !(t instanceof TypeParameter) ) { //don't look for members in a type parameter with a self-referential lower bound
                for (Declaration d: t.getMembers(name, visited)) {
                    if (d.isShared() && isResolvable(d)) {
                        if (!contains(members, d)) {
                        	members.add(d);
                        }
                    }
                }
            //}
        }
        TypeDeclaration et = getExtendedTypeDeclaration();
        if (et!=null) {
            for (Declaration d: et.getMembers(name, visited)) {
                if (d.isShared() && isResolvable(d)) {
                    if (!contains(members, d)) {
                    	members.add(d);
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
        return isMember(dec, new ArrayList<TypeDeclaration>());
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
        for (TypeDeclaration t: getSatisfiedTypeDeclarations()) {
            if (t.isMember(dec, visited)) {
                return true;
            }
        }
        TypeDeclaration et = getExtendedTypeDeclaration();
        if (et!=null) {
            if (et.isMember(dec, visited)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Does the given declaration inherit the given type?
     */
    public boolean inherits(TypeDeclaration dec) {
        return inherits(dec, new ArrayList<TypeDeclaration>());
    }
    
    private boolean inherits(TypeDeclaration dec, List<TypeDeclaration> visited) {
        if (visited.contains(this)) {
            return false;
        }
        visited.add(this);
        if (this instanceof ClassOrInterface && 
        	dec instanceof ClassOrInterface && 
        		equals(dec)) {
        	return true;
        }
        for (TypeDeclaration t: getSatisfiedTypeDeclarations()) {
            if (t.inherits(dec, visited)) {
                return true;
            }
        }
        TypeDeclaration et = getExtendedTypeDeclaration();
        if (et!=null) {
            if (et.inherits(dec, visited)) {
                return true;
            }
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
                new ArrayList<TypeDeclaration>());
    }

    private Declaration getRefinedMember(String name, 
            List<ProducedType> signature, boolean ellipsis, List<TypeDeclaration> visited) {
        if (visited.contains(this)) {
            return null;
        }
        else {
            visited.add(this);
            TypeDeclaration et = getExtendedTypeDeclaration();
            if (et!=null) {
                Declaration ed = et.getRefinedMember(name, signature, ellipsis, visited);
                if (ed!=null) {
                    return ed;
                }
            }
            for (TypeDeclaration st: getSatisfiedTypeDeclarations()) {
                Declaration sd = st.getRefinedMember(name, signature, ellipsis, visited);
                if (sd!=null) {
                    return sd;
                }
            }
            return getDirectMember(name, signature, ellipsis);
        }
    }
    
    /**
     * Get the most-refined member with the given name,
     * searching this type first, taking aliases into
     * account, followed by supertypes. We're looking
     * for shared members.
     */
    public Declaration getMember(String name, Unit unit, 
            List<ProducedType> signature, boolean variadic) {
        //TODO: does not handle aliased members of supertypes
        Declaration d = unit.getImportedDeclaration(this, name, signature, variadic);
        if (d==null) {
            return getMemberInternal(name, signature, variadic).getMember();
        }
        else {
            return d;
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
    public boolean isMemberAmbiguous(String name, Unit unit, 
            List<ProducedType> signature, boolean variadic) {
        //TODO: does not handle aliased members of supertypes
        Declaration d = unit.getImportedDeclaration(this, name, signature, variadic);
        if (d==null) {
            return getMemberInternal(name, signature, variadic).isAmbiguous();
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
    public Declaration getMember(String name, 
            List<ProducedType> signature, boolean variadic) {
        return getMemberInternal(name, signature, variadic).getMember();
    }

    private SupertypeDeclaration getMemberInternal(String name,
            List<ProducedType> signature, boolean variadic) {
        //first search for the member in the local
        //scope, including non-shared declarations
        Declaration d = getDirectMember(name, signature, variadic);
        if (d!=null && d.isShared()) {
            //if it's shared, it's what we're 
            //looking for, return it
            //TODO: should also return it if we're 
            //      calling from local scope!
            if (signature!=null && isAbstraction(d)){
                //look for a supertype decl that matches the signature better
                SupertypeDeclaration sd = getSupertypeDeclaration(name, signature, variadic);
                Declaration sm = sd.getMember();
                if (sm!=null && !isAbstraction(sm)) {
                    return sd;
                }
            }
            return new SupertypeDeclaration(d, false);
        }
        else {
            //now look for inherited shared declarations
            SupertypeDeclaration sd = getSupertypeDeclaration(name, signature, variadic);
            if (sd.getMember()!=null || sd.isAmbiguous()) {
                return sd;
            }
        }
        //finally return the non-shared member we
        //found earlier, so that the caller can give
        //a nice error message
        return new SupertypeDeclaration(d, false);
    }
    
    /**
     * Get the parameter or most-refined member with the 
     * given name, searching this type first, followed by 
     * supertypes. Return un-shared members declared by
     * this type.
     */
    @Override
    protected Declaration getMemberOrParameter(String name, 
            List<ProducedType> signature, boolean variadic) {
        //first search for the member or parameter 
        //in the local scope, including non-shared 
        //declarations
        Declaration d = getDirectMember(name, signature, variadic);
        if (d!=null) {
            if (signature!=null && isAbstraction(d)){
                // look for a supertype decl that matches the signature better
                Declaration s = getSupertypeDeclaration(name, signature, variadic).getMember();
                if (s!=null && !isAbstraction(s)) {
                    return s;
                }
            }
            return d;
        }
        else {
            //now look for inherited shared declarations
            return getSupertypeDeclaration(name, signature, variadic).getMember();
        }
    }

    /**
     * Is the given declaration inherited from
     * a supertype of this type or an outer
     * type?
     */
    @Override
    public boolean isInherited(Declaration d) {
        if (d.getContainer().equals(this)) {
            return false;
        }
        else if (isInheritedFromSupertype(d)) {
            return true;
        }
        else if (getContainer()!=null) {
            return getContainer().isInherited(d);
        }
        else {
            return false;
        }
    }

    /**
     * Get the containing type which inherits the given declaration.
     * Returns null if the declaration is not inherited!!
     */
    @Override
    public TypeDeclaration getInheritingDeclaration(Declaration d) {
        if (d.getContainer().equals(this)) {
            return null;
        }
        else if (isInheritedFromSupertype(d)) {
            return this;
        }
        else if (getContainer()!=null) {
            return getContainer().getInheritingDeclaration(d);
        }
        else {
            return null;
        }
    }

    public boolean isInheritedFromSupertype(final Declaration member) {
        final List<ProducedType> signature = getSignature(member);
        class Criteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                if (type.equals(TypeDeclaration.this)) {
                    return false;
                }
                else {
                    Declaration dm = type.getDirectMember(member.getName(), signature, false);
                    return dm!=null && dm.equals(member);
                }
            }
            @Override
            public boolean isMemberLookup() {
            	return false;
            }
        };
        return getType().getSupertype(new Criteria())!=null;
    }
    
    private static class SupertypeDeclaration {
        private Declaration member;
        private boolean ambiguous;
        private SupertypeDeclaration(Declaration declaration, boolean ambiguous) {
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
     * @param signature 
     */
    private SupertypeDeclaration getSupertypeDeclaration(final String name, final List<ProducedType> signature, final boolean variadic) {
        class ExactCriteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                // do not look in ourselves
                if (type == TypeDeclaration.this) {
                    return false;
                }
                Declaration d = type.getDirectMember(name, signature, variadic);
                if (d!=null && d.isShared() && isResolvable(d)) {
                    // only accept abstractions if we don't have a signature
                    return !isAbstraction(d) || signature == null;
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
        class LooseCriteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                // do not look in ourselves
                if (type == TypeDeclaration.this) {
                    return false;
                }
                Declaration d = type.getDirectMember(name, null, false);
                if (d!=null && d.isShared() && isResolvable(d)) {
                    // only accept abstractions
                    return isAbstraction(d);
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
        ProducedType st = getType().getSupertype(new ExactCriteria());
        if (st == null) {
            //try again, ignoring the given signature
            st = getType().getSupertype(new LooseCriteria());
        }
        if (st == null) {
            //no such member
            return new SupertypeDeclaration(null, false);
        }
        else if (st.getDeclaration() instanceof UnknownType) {
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
            Declaration member = st.getDeclaration()
                    .getDirectMember(name, signature, variadic);
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
    
    public Map<String, DeclarationWithProximity> getImportableDeclarations(Unit unit, String startingWith, List<Import> imports, int proximity) {
        //TODO: fix copy/paste from below!
        Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && d.isShared() && 
            		!isOverloadedVersion(d) &&
                    isNameMatching(startingWith, d) ) {
                boolean already = false;
                for (Import i: imports) {
                    if (i.getDeclaration().equals(d)) {
                        already = true;
                        break;
                    }
                }
                if (!already) {
                    result.put(d.getName(), new DeclarationWithProximity(d, proximity));
                }
            }
        }
        return result;
    }
    
    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
        Map<String, DeclarationWithProximity> result = super.getMatchingDeclarations(unit, startingWith, proximity);
        //Inherited declarations hide outer and imported declarations
        result.putAll(getMatchingMemberDeclarations(null, startingWith, proximity));
        //Local declarations always hide inherited declarations, even if non-shared
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && !isOverloadedVersion(d) &&
            		isNameMatching(startingWith, d)) {
                result.put(d.getName(), 
                		new DeclarationWithProximity(d, proximity));
            }
        }
        return result;
    }

    public Map<String, DeclarationWithProximity> getMatchingMemberDeclarations(Scope scope, String startingWith, int proximity) {
        Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (TypeDeclaration st: getSatisfiedTypeDeclarations()) {
            mergeMembers(result, st.getMatchingMemberDeclarations(scope, startingWith, proximity+1));
        }
        TypeDeclaration et = getExtendedTypeDeclaration();
        if (et!=null) {
            mergeMembers(result, et.getMatchingMemberDeclarations(scope, startingWith, proximity+1));
        }
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && 
                    !isOverloadedVersion(d) &&
                    isNameMatching(startingWith, d)) {
                if( d.isShared() || Util.contains(d.getScope(), scope) ) {
                    result.put(d.getName(), new DeclarationWithProximity(d, proximity));
                }
            }
        }
        //TODO: self type?
        return result;
    }

    private void mergeMembers(Map<String, DeclarationWithProximity> result,
            Map<String, DeclarationWithProximity> etm) {
        for (Map.Entry<String, DeclarationWithProximity> e: etm.entrySet()) {
            DeclarationWithProximity dwp = result.get(e.getKey());
            if (dwp==null || !dwp.getDeclaration().refines(e.getValue().getDeclaration())) {
                result.put(e.getKey(), e.getValue());
            }
        }
    }
    
    /**
     * Be very careful with this operation. It does NOT return
     * the right result for union types! It's really only for
     * use by Util.haveUninhabitableIntersection().
     */
    public List<TypeDeclaration> getSuperTypeDeclarations() {
    	//TODO: do we need to handle union types here?
    	List<TypeDeclaration> result;
    	ClassOrInterface etd = getExtendedTypeDeclaration();
		if (etd==null) {
    		result = new ArrayList<TypeDeclaration>();
    	}
		else {
			result = etd.getSuperTypeDeclarations();
		}
    	for (TypeDeclaration std: getSatisfiedTypeDeclarations()) {
    		result.addAll(std.getSuperTypeDeclarations());
    	}
    	if (this instanceof ClassOrInterface) {
    		result.add(this);
    	}
    	return result;
    }

    /**
     * Clears the ProducedType supertype caches for that declaration. Does nothing
     * for Union/Intersection types since they are not cached. Only does something
     * for ClassOrInterface, TypeAlias, TypeParameter.
     */
    public void clearProducedTypeCache() {
        // do nothing, work in subclasses
    }

}
