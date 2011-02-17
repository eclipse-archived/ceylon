package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeDeclaration extends Declaration implements Generic {
	
	ProducedType extendedType;
	List<ProducedType> satisfiedTypes = new ArrayList<ProducedType>();
	List<ProducedType> caseTypes = new ArrayList<ProducedType>();
	List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();
	
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
	
}
