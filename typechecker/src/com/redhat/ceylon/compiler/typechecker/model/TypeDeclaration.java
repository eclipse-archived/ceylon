package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isAbstraction;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isNameMatching;
import static com.redhat.ceylon.compiler.typechecker.model.Util.isResolvable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class TypeDeclaration extends Declaration 
        implements ImportableScope, Generic, Cloneable {

    private ProducedType extendedType;
    private List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
    private List<ProducedType> caseTypes = null;
    private List<TypeParameter> typeParameters = Collections.emptyList();
    private ProducedType selfType;
    
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

    public List<TypeParameter> getTypeParameters() {
        return typeParameters;
    }

    public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
    }

    public ClassOrInterface getExtendedTypeDeclaration() {
        if (getExtendedType()==null) {
            return null;
        }
        else {
            return (ClassOrInterface) getExtendedType().getDeclaration();
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
        List<ProducedType> caseTypes = getCaseTypes();
        if (caseTypes==null) {
            return null;
        }
        else {
            for (ProducedType pt: caseTypes) {
                list.add(pt.getDeclaration());
            }
        }
        return list;
    }

    public List<ProducedType> getCaseTypes() {
        return caseTypes;
    }

    public void setCaseTypes(List<ProducedType> caseTypes) {
        this.caseTypes = caseTypes;
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
        TypeDeclaration td = this;
        if (!getTypeParameters().isEmpty()) {
            int last = getTypeParameters().size()-1;
            int lastArg = typeArguments.size()-1;
            TypeParameter tp = getTypeParameters().get(last);
            if (tp.isSequenced() && lastArg>last) {
                //there are multiple args to sequenced param
                td = clone();
                td.typeParameters = new ArrayList<TypeParameter>();
                td.typeParameters.addAll(getTypeParameters());
                for (int i=1; i<=lastArg-last; i++) {
                    TypeParameter tpc = (TypeParameter) tp.clone();
                    tpc.setName(tp.getName()+i);
                    td.getTypeParameters().add(tpc);
                }
            }
            else if (tp.isSequenced() && lastArg==last-1) {
                //there is no arg to sequenced param
                td = clone();
                td.typeParameters = new ArrayList<TypeParameter>();
                td.typeParameters.addAll(getTypeParameters());
                td.typeParameters.remove(tp);
            }
        }
        ProducedType pt = new ProducedType();
        pt.setDeclaration(td);
        pt.setQualifyingType(qualifyingType);
        pt.setTypeArguments(arguments(td, qualifyingType, typeArguments));
        return pt;
    }
    
    @Override
    public boolean equals(Object object) {
        return super.equals(object) && 
                getTypeParameters().size()==
                    ((TypeDeclaration) object).getTypeParameters().size();
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
     * directly by this type. 
     */
    public List<Declaration> getInheritedMembers(String name) {
        return getInheritedMembers(name, new ArrayList<TypeDeclaration>());
    }
    
    private List<Declaration> getInheritedMembers(String name, 
            List<TypeDeclaration> visited) {
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
    public Declaration getRefinedMember(String name, 
            List<ProducedType> signature) {
        return getRefinedMember(name, signature, 
                new ArrayList<TypeDeclaration>());
    }

    private Declaration getRefinedMember(String name, 
            List<ProducedType> signature, List<TypeDeclaration> visited) {
        if (visited.contains(this)) {
            return null;
        }
        else {
            visited.add(this);
            TypeDeclaration et = getExtendedTypeDeclaration();
            if (et!=null) {
                Declaration ed = et.getRefinedMember(name, signature, visited);
                if (ed!=null) {
                    return ed;
                }
            }
            for (TypeDeclaration st: getSatisfiedTypeDeclarations()) {
                Declaration sd = st.getRefinedMember(name, signature, visited);
                if (sd!=null) {
                    return sd;
                }
            }
            return getDirectMember(name, signature);
        }
    }
    
    /**
     * Get the most-refined member with the given name,
     * searching this type first, taking aliases into
     * account, followed by supertypes.
     */
    public Declaration getMember(String name, Unit unit, 
            List<ProducedType> signature) {
        //TODO: does not handle aliased members of supertypes
        Declaration d = unit.getImportedDeclaration(this, name, signature);
        if (d==null) {
            return getMember(name, signature);
        }
        else {
            return d;
        }
    }

    /**
     * Get the most-refined member with the given name,
     * searching this type first, followed by supertypes.
     */
    @Override
    public Declaration getMember(String name, 
            List<ProducedType> signature) {
        //first search for the member in the local
        //scope, including non-shared declarations
        Declaration d = getDirectMember(name, signature);
        if (d==null) d = getDirectMemberOrParameter(name, signature);
        if (d!=null && d.isShared()) {
            //if it's shared, it's what we're 
            //looking for, return it
            //TODO: should also return it if we're 
            //      calling from local scope!
            if (signature!=null && isAbstraction(d)){
                // look for a supertype decl that matches the signature better
                Declaration s = getSupertypeDeclaration(name, signature);
                if (s!=null && !isAbstraction(s)) {
                    return s;
                }
            }
            return d;
        }
        else {
            //now look for inherited shared declarations
            Declaration s = getSupertypeDeclaration(name, signature);
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
    public Declaration getMemberOrParameter(String name, 
            List<ProducedType> signature) {
        //first search for the member or parameter 
        //in the local scope, including non-shared 
        //declarations
        Declaration d = getDirectMemberOrParameter(name, signature);
        if (d!=null) {
            if (signature!=null && isAbstraction(d)){
                // look for a supertype decl that matches the signature better
                Declaration s = getSupertypeDeclaration(name, signature);
                if (s!=null && !isAbstraction(s)) {
                    return s;
                }
            }
            return d;
        }
        else {
            //now look for inherited shared declarations
            return getSupertypeDeclaration(name, signature);
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
        class Criteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                if (type.equals(TypeDeclaration.this)) {
                    return false;
                }
                else {
                    Declaration dm = type.getDirectMember(member.getName(), null);
                    return dm!=null && dm.equals(member);
                }
            }
        };
        return getType().getSupertype(new Criteria())!=null;
    }

    /**
     * Get the supertype which defines the most-refined
     * member with the given name.
     * @param signature 
     */
    private Declaration getSupertypeDeclaration(final String name, final List<ProducedType> signature) {
        class Criteria implements ProducedType.Criteria {
            @Override
            public boolean satisfies(TypeDeclaration type) {
                // do not look in ourselves
                if (type == TypeDeclaration.this)
                    return false;
                Declaration d = type.getDirectMember(name, signature);
                if (d!=null && d.isShared()) {
                    // only accept abstractions if we don't have a signature
                    return !Util.isAbstraction(d) || signature == null;
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
            return st.getDeclaration().getDirectMember(name, signature);
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
    
    public Map<String, DeclarationWithProximity> getImportableDeclarations(Unit unit, String startingWith, List<Import> imports, int proximity) {
        //TODO: fix copy/paste from below!
        Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && d.isShared() && 
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
        result.putAll(getMatchingMemberDeclarations(startingWith, proximity));
        //Local declarations always hide inherited declarations, even if non-shared
        for (Declaration d: getMembers()) {
            if (isResolvable(d) && isNameMatching(startingWith, d)) {
                result.put(d.getName(), new DeclarationWithProximity(d, proximity));
            }
        }
        return result;
    }

    public Map<String, DeclarationWithProximity> getMatchingMemberDeclarations(String startingWith, int proximity) {
        Map<String, DeclarationWithProximity> result = new TreeMap<String, DeclarationWithProximity>();
        for (TypeDeclaration st: getSatisfiedTypeDeclarations()) {
            mergeMembers(result, st.getMatchingMemberDeclarations(startingWith, proximity+1));
        }
        TypeDeclaration et = getExtendedTypeDeclaration();
        if (et!=null) {
            mergeMembers(result, et.getMatchingMemberDeclarations(startingWith, proximity+1));
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

    private void mergeMembers(Map<String, DeclarationWithProximity> result,
            Map<String, DeclarationWithProximity> etm) {
        for (Map.Entry<String, DeclarationWithProximity> e: etm.entrySet()) {
            DeclarationWithProximity dwp = result.get(e.getKey());
            if (dwp==null || !dwp.getDeclaration().refines(e.getValue().getDeclaration())) {
                result.put(e.getKey(), e.getValue());
            }
        }
    }

}
