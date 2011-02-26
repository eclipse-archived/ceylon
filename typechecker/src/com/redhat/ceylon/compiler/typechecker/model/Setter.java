package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An attribute setter.
 * 
 * @author Gavin King
 *
 */
public class Setter extends MethodOrValue implements Scope {
    
	List<Declaration> members = new ArrayList<Declaration>();
	Getter getter;

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

	public Getter getGetter() {
		return getter;
	}
	
	public void setGetter(Getter getter) {
		this.getter = getter;
	}
	
	@Override
	public List<String> getQualifiedName() {
	    return getter.getQualifiedName();
	}
	
}
