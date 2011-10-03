package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class TypeDeclaration extends Declaration implements Scope, Generic {

    private ProducedType extendedType;
    private List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
    private List<ProducedType> caseTypes = null;
    private List<TypeParameter> typeParameters = Collections.emptyList();
    private ProducedType selfType;

    @Override
    public boolean isParameterized() {
        return !typeParameters.isEmpty();
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }

    public Class getExtendedTypeDeclaration() {
        if (getExtendedType()==null) {
            return null;
        }
        else {
            return (Class) getExtendedType().getDeclaration();
        }
    }

    public ProducedType getExtendedType() {
        return extendedType;
    }

    public void setExtendedType(ProducedType extendedType) {
        this.extendedType = extendedType;
    }

    public List<TypeDeclaration> getSatisfiedTypeDeclarations() {
        List<TypeDeclaration> list = new ArrayList<TypeDeclaration>();
        for (ProducedType pt: getSatisfiedTypes()) {
            list.add(pt.getDeclaration());
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
        List<TypeDeclaration> list = new ArrayList<TypeDeclaration>();
        for (ProducedType pt: getCaseTypes()) {
            list.add(pt.getDeclaration());
        }
        return list;
    }

    public List<ProducedType> getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(List<ProducedType> caseTypes) {
        this.caseTypes = caseTypes;
    }
    
    public Set<TypeDeclaration> getKnownSubtypes() {
        return Collections.emptySet();
    }

    @Override
    public ProducedReference getProducedReference(ProducedType pt,
            List<ProducedType> typeArguments) {
        return getProducedType(pt, typeArguments);
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
        /*if (!acceptsArguments(this, typeArguments)) {
              return null;
          }*/
        ProducedType pt = new ProducedType();
        pt.setDeclaration(this);
        pt.setQualifyingType(qualifyingType);
        pt.setTypeArguments(arguments(this, qualifyingType, typeArguments));
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
        ProducedType pt = new ProducedType();
        if (isMember()) {
            pt.setQualifyingType(((ClassOrInterface) getContainer()).getType());
        }
        pt.setDeclaration(this);
        //each type parameter is its own argument
        Map<TypeParameter, ProducedType> map = new HashMap<TypeParameter, ProducedType>();
        for (TypeParameter p: getTypeParameters()) {
            ProducedType pta = new ProducedType();
            pta.setDeclaration(p);
            map.put(p, pta);
        }
        pt.setTypeArguments(map);
        return pt;
    }

    private List<Declaration> getMembers(String name, List<TypeDeclaration> visited) {
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
     * directly by this type. 
     */
    public List<Declaration> getInheritedMembers(String name) {
        return getInheritedMembers(name, new ArrayList<TypeDeclaration>());
    }
    
    private List<Declaration> getInheritedMembers(String name, List<TypeDeclaration> visited) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (TypeDeclaration t: getSatisfiedTypeDeclarations()) {
            //if ( !(t instanceof TypeParameter) ) { //don't look for members in a type parameter with a self-referential lower bound
                for (Declaration d: t.getMembers(name, visited)) {
                    if (d.isShared() && isResolvable(d)) {
                        members.add(d);
                    }
                }
            //}
        }
        TypeDeclaration et = getExtendedTypeDeclaration();
        if (et!=null) {
            for (Declaration d: et.getMembers(name, visited)) {
                if (d.isShared() && isResolvable(d)) {
                    members.add(d);
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
    
    private boolean isMember(Declaration dec, List<TypeDeclaration> visited) {
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
        if (equals(dec)) return true;
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
    public Declaration getRefinedMember(String name) {
    	return getRefinedMember(name, new ArrayList<TypeDeclaration>());
    }

    private Declaration getRefinedMember(String name, List<TypeDeclaration> visited) {
    	if (visited.contains(this)) {
    		return null;
    	}
    	else {
	    	visited.add(this);
	    	TypeDeclaration et = getExtendedTypeDeclaration();
	    	if (et!=null) {
				Declaration ed = et.getRefinedMember(name, visited);
		    	if (ed!=null) {
		    		return ed;
		    	}
	    	}
			for (TypeDeclaration st: getSatisfiedTypeDeclarations()) {
				Declaration sd = st.getRefinedMember(name, visited);
				if (sd!=null) {
					return sd;
				}
			}
	    	return getDirectMember(name);
    	}
    }

    /**
     * Get the most-refined member with the given name,
     * searching this type first, followed by supertypes.
     */
    @Override
    public Declaration getMember(String name) {
        //first search for the member in the local
        //scope, including non-shared declarations
        Declaration d = getDirectMember(name);
        if (d==null) d = getDirectMemberOrParameter(name);
        if (d!=null && d.isShared()) {
            //if it's shared, it's what we're 
            //looking for, return it
            //TODO: should also return it if we're 
            //      calling from local scope!
            return d;
        }
        else {
            //now look for inherited shared declarations
            Declaration s = getSupertypeDeclaration(name);
            if (s!=null) {
                return s;
            }
        }
        //finally return the non-shared member we
        //found earlier, so that the caller can give
        //a nice error message
        return d;
    }
    
    /**
     * Get the parameter or most-refined member with the 
     * given name, searching this type first, followed by 
     * supertypes.
     */
    @Override
    public Declaration getMemberOrParameter(String name) {
        //first search for the member or parameter 
        //in the local scope, including non-shared 
        //declarations
        Declaration d = getDirectMemberOrParameter(name);
        if (d!=null) {
            return d;
        }
        else {
            //now look for inherited shared declarations
            return getSupertypeDeclaration(name);
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
     * Get the containing type which inherits
     * the given declaration.
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
        class Criteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                if (type.equals(TypeDeclaration.this)) {
                    return false;
                }
                else {
                    Declaration dm = type.getDirectMember(member.getName());
                    return dm!=null && dm.equals(member);
                }
            }
        };
        return getType().getSupertype(new Criteria())!=null;
    }

    /**
     * Get the supertype which defines the most-refined
     * member with the given name. 
     */
    private Declaration getSupertypeDeclaration(final String name) {
        class Criteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                Declaration d = type.getDirectMember(name);
                if (d!=null && d.isShared()) {
                    return true;
                }
                else {
                    return false;
                }
            }
        };
        //this works by finding the most-specialized supertype
        //that defines the member
        ProducedType st = getType().getSupertype(new Criteria());
        if (st!=null) {
            return st.getDeclaration().getDirectMember(name);
        }
        else {
            return null;
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
    
    @Override
    public Map<String, DeclarationWithProximity> getMatchingDeclarations(Unit unit, String startingWith, int proximity) {
    	Map<String, DeclarationWithProximity> result = getMatchingMemberDeclarations(startingWith, proximity);
    	//TODO: is this correct? I thought inherited declarations hide outer
    	//      declarations! I think this is a bug
    	result.putAll(super.getMatchingDeclarations(unit, startingWith, proximity));
    	return result;
    }

	public Map<String, DeclarationWithProximity> getMatchingMemberDeclarations(String startingWith, int proximity) {
		Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        TypeDeclaration et = getExtendedTypeDeclaration();
    	for (TypeDeclaration st: getSatisfiedTypeDeclarations()) {
    	    //TODO: account for the case where one interface refines
    	    //      a formal member of a second interface
    		result.putAll(st.getMatchingMemberDeclarations(startingWith, proximity+1));
    	}
        if (et!=null) {
            //TODO: Object has a formal declaration of "string", that might 
            //      be refined by an interface, in which case we should ignore
            //      it here
            result.putAll(et.getMatchingMemberDeclarations(startingWith, proximity+1));
        }
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && d.isShared() && 
            		isNameMatching(startingWith, d)) {
                result.put(d.getName(), new DeclarationWithProximity(d, proximity));
            }
        }
    	//TODO: self type?
    	return result;
	}

}
