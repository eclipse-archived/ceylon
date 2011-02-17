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
public class Method extends MethodOrValue implements Generic, Scope, Functional {
	
    Boolean formal;
    
    List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();	
    List<ParameterList> parameterLists = new ArrayList<ParameterList>();
    List<Declaration> members = new ArrayList<Declaration>();

	public Boolean isFormal() {
		return formal;
	}
	
	public void setFormal(Boolean formal) {
		this.formal = formal;
	}
	
	public ProducedType getType() {
		return type;
	}
	
	public void setType(ProducedType type) {
		this.type = type;
	}
	
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	
	@Override
	public List<Declaration> getMembers() {
	    return members;
	}
	
	@Override
	public List<ParameterList> getParameterLists() {
	    return parameterLists;
	}
	
	@Override
	public void addParameterList(ParameterList pl) {
	    parameterLists.add(pl);
	}

}
