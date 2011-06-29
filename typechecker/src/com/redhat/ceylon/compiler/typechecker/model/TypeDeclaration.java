package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TypeDeclaration extends Declaration implements Scope, Generic {
	
	ProducedType extendedType;
	List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
	List<ProducedType> caseTypes = Collections.emptyList();
	List<TypeParameter> typeParameters = Collections.emptyList();
	
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
     * @param outerType the qualifying produced 
     *        type or null if this is not a
     *        nested type declaration
     * @param typeArguments arguments to the type 
     *        parameters of this declaration
     */
	public ProducedType getProducedType(ProducedType outerType, 
	        List<ProducedType> typeArguments) {
	    /*if (!acceptsArguments(this, typeArguments)) {
	        return null;
	    }*/
	    ProducedType pt = new ProducedType();
	    pt.setDeclaration(this);
	    pt.setDeclaringType(outerType);
	    pt.setTypeArguments( arguments(this, outerType, typeArguments) );
	    return pt;
	}
	
	/**
	 * The type of the declaration as seen from 
	 * within the body of the declaration itself.
	 * 
	 * Note that for certain special types which
	 * we happen to know don't have type arguments, 
	 * we use this as a convenience method to 
	 * quickly get a produced type for use outside
	 * the body of the declaration, but this is not
	 * really correct!
	 */
    public ProducedType getType() {
        ProducedType pt = new ProducedType();
        if (isMemberType()) {
            pt.setDeclaringType( ( (ClassOrInterface) getContainer() ).getType() ); 
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
    
    public boolean isMemberType() {
        return false;
    }
    
    private List<Declaration> getMembers(String name) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (Declaration d: getMembers()) {
            if (d.getName().equals(name)) {
                members.add(d);
            }
        }
        if (members.isEmpty()) {
            members.addAll(getInheritedMembers(name));
        }
        return members;
    }
    
    public List<Declaration> getInheritedMembers(String name) {
        List<Declaration> members = new ArrayList<Declaration>();
        for (ProducedType t: getSatisfiedTypes()) {
            for (Declaration d: t.getDeclaration().getMembers(name)) {
                if (d.isShared()) {
                    members.add(d);
                }
            }
        }
        if (getExtendedType()!=null) {
            for (Declaration d: getExtendedType().getDeclaration().getMembers(name)) {
                if (d.isShared()) {
                    members.add(d);
                }
            }
        }
        return members;
    }

    @Override
    public Declaration getMember(String name) {
        Declaration d = super.getMember(name);
        if (d!=null) {
            return d;
        }
        else {
            return this.getSupertypeDeclaration(name);
        }
    }

    @Override
    public Declaration getMemberOrParameter(String name) {
        Declaration d = super.getMemberOrParameter(name);
        if (d!=null) {
            return d;
        }
        else {
            return this.getSupertypeDeclaration(name);
        }
    }

    public Declaration getSupertypeDeclaration(String name) {
        for (ProducedType st: getType().getSupertypes()) {
            TypeDeclaration std = st.getDeclaration();
            if (std!=this) {
                Declaration d = std.getMember(name);
                if (d!=null && d.isShared()) {
                    return d;
                }
            }
        }
        return null;
    }
    
}
