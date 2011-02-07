package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Type extends Model {
	
	List<Type> typeArguments = new ArrayList<Type>();
	GenericType genericType;
	
	public GenericType getGenericType() {
		return genericType;
	}
	
	public void setGenericType(GenericType type) {
		this.genericType = type;
	}
	
	public List<Type> getTypeArguments() {
		return typeArguments;
	}
	
	@Override
	public String toString() {
		return genericType.getName() + 
			typeArguments.toString().replace('[', '<').replace(']', '>');
	}
	
}
