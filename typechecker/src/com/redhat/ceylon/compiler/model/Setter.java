package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An attribute setter.
 * 
 * @author Gavin King
 *
 */
public class Setter extends Declaration implements Scope<Declaration> {
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
}
