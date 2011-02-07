package com.redhat.ceylon.compiler.model;

import java.util.List;

/**
 * A method. Note that a method must have
 * at least one parameter list.
 * 
 * @author Gavin King
 *
 */
public class Method extends Functional implements Generic {
	List<TypeParameter> typeParameters;
	Boolean formal;
	
	public Boolean isFormal() {
		return formal;
	}
	
	public void setFormal(Boolean formal) {
		this.formal = formal;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}

}
