package com.redhat.ceylon.compiler.typechecker.model;

import static com.redhat.ceylon.compiler.typechecker.model.Util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TypeDeclaration extends Declaration implements Generic, Scope {
	
	ProducedType extendedType;
	List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
	List<ProducedType> caseTypes = Collections.emptyList();
	List<TypeParameter> typeParameters = Collections.emptyList();
	
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
	
	public ProducedType getProducedType(ProducedType outerType, List<ProducedType> typeArguments) {
	    /*if (!acceptsArguments(this, typeArguments)) {
	        return null;
	    }*/
	    ProducedType pt = new ProducedType();
	    pt.setDeclaration(this);
	    pt.setDeclaringType(outerType);
	    pt.setTypeArguments( arguments(this, outerType, typeArguments) );
	    return pt;
	}
	
    public ProducedType getType() {
        ProducedType pt = new ProducedType();
        if (isMemberType()) {
            pt.setDeclaringType( ( (ClassOrInterface) getContainer() ).getType() ); 
        }
        pt.setDeclaration(this);
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
            members.addAll( t.getDeclaration().getMembers(name) );
        }
        if (getExtendedType()!=null) {
            members.addAll( getExtendedType().getDeclaration().getMembers(name) );
        }
        return members;
    }

}
