package com.redhat.ceylon.compiler.model;


/**
 * Anything which includes a type declaration: 
 * a method, attribute, parameter, or local.
 * @author Gavin King
 *
 */
public class Typed extends Declaration {
	
	Type type;

	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}

}
