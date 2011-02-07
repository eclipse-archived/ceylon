package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class GenericType extends Declaration implements Generic {
	
	Type extendedType;
	List<Type> satisfiedTypes = new ArrayList<Type>();
	List<Type> caseTypes = new ArrayList<Type>();
	List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	public void setTypeParameters(List<TypeParameter> typeParameters) {
		this.typeParameters = typeParameters;
	}
	public Type getExtendedType() {
		return extendedType;
	}
	public void setExtendedType(Type extendedType) {
		this.extendedType = extendedType;
	}
	public List<Type> getSatisfiedTypes() {
		return satisfiedTypes;
	}
	public void setSatisfiedTypes(List<Type> satisfiedTypes) {
		this.satisfiedTypes = satisfiedTypes;
	}
	public List<Type> getCaseTypes() {
		return caseTypes;
	}
	public void setCaseTypes(List<Type> caseTypes) {
		this.caseTypes = caseTypes;
	}
	
}
