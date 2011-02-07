package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An attribute setter.
 * 
 * @author Gavin King
 *
 */
public class Setter extends Declaration implements Scope<Structure> {
	List<Structure> members = new ArrayList<Structure>();
	Getter getter;

	@Override
	public List<Structure> getMembers() {
		return members;
	}

	public Getter getGetter() {
		return getter;
	}
	
	public void setGetter(Getter getter) {
		this.getter = getter;
	}
}
