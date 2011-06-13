package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A method. Note that a method must have
 * at least one parameter list.
 * 
 * @author Gavin King
 *
 */
public class Method extends MethodOrValue implements Generic, Scope, Functional {
	
    //boolean formal;
    
    List<TypeParameter> typeParameters = Collections.emptyList();	
    List<ParameterList> parameterLists = new ArrayList<ParameterList>();
    List<Declaration> members = new ArrayList<Declaration>();

	/*public boolean isFormal() {
		return formal;
	}
	
	public void setFormal(boolean formal) {
		this.formal = formal;
	}*/
	
	public ProducedType getType() {
		return type;
	}
	
	public void setType(ProducedType type) {
		this.type = type;
	}
	
    @Override
    public boolean isParameterized() {
        return !typeParameters.isEmpty();
    }
    
	public List<TypeParameter> getTypeParameters() {
		return typeParameters;
	}
	
	public void setTypeParameters(List<TypeParameter> typeParameters) {
        this.typeParameters = typeParameters;
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
