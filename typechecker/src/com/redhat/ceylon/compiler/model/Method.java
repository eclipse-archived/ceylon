package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A method. Note that a method must have
 * at least one parameter list.
 * 
 * @author Gavin King
 *
 */
public class Method extends Functional implements Scope<Declaration>, Generic {
	List<TypeParameter> typeParameters;
	List<Declaration> members = new ArrayList<Declaration>();
	Boolean formal;
	
	public Boolean isFormal() {
		return formal;
	}
	
	public void setFormal(Boolean formal) {
		this.formal = formal;
	}
	
	@Override
	public List<Declaration> getMembers() {
		return members;
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
