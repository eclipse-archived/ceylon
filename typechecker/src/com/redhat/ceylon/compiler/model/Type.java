package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Type {
	
	GenericType type;
	List<Type> typeArguments = new ArrayList<Type>();
	
	public GenericType getType() {
		return type;
	}
	public void setType(GenericType type) {
		this.type = type;
	}
	
	public List<Type> getTypeArguments() {
		return typeArguments;
	}
}
