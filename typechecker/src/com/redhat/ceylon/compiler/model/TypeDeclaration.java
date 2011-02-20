package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TypeDeclaration extends Declaration implements Generic, Scope {
	
	ProducedType extendedType;
	List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
	List<ProducedType> caseTypes = new ArrayList<ProducedType>();
	List<TypeParameter> typeParameters = Collections.emptyList();
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	
	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}
	
	public ProducedType getExtendedType() {
		return extendedType;
	}
	
	public void setExtendedType(ProducedType extendedType) {
		this.extendedType = extendedType;
	}
	
	public List<ProducedType> getSatisfiedTypes() {
		return satisfiedTypes;
	}
	
	public void setSatisfiedTypes(List<ProducedType> satisfiedTypes) {
		this.satisfiedTypes = satisfiedTypes;
	}
	
	public List<ProducedType> getCaseTypes() {
		return caseTypes;
	}
	
	public void setCaseTypes(List<ProducedType> caseTypes) {
		this.caseTypes = caseTypes;
	}
	
	public ProducedType getProducedType(List<ProducedType> typeArguments) {
	    if (!Util.acceptsArguments(this, typeArguments)) {
	        return null;
	    }
	    ProducedType pt = new ProducedType();
	    pt.setDeclaration(this);
	    pt.setTypeArguments( Util.arguments(this, typeArguments) );
	    return pt;
	}
	
    public ProducedType getType() {
        ProducedType pt = new ProducedType();
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
    
}
