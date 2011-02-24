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
	List<ProducedType> caseTypes = null;
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

}
