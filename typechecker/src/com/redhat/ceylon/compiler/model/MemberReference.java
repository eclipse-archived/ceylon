package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class MemberReference extends Model {
	
	List<ProducedType> typeArguments = new ArrayList<ProducedType>();
	Declaration declaration;
	
	public Declaration getDeclaration() {
		return declaration;
	}
	
	public void setDeclaration(Declaration type) {
		this.declaration = type;
	}
	
	public List<ProducedType> getTypeArguments() {
		return typeArguments;
	}
		
}
