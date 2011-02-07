package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An attribute getter.
 * 
 * @author Gavin King
 *
 */
public class Getter extends Typed implements Scope {
	
	List<Structure> members = new ArrayList<Structure>();

	@Override
	public List<Structure> getMembers() {
		return members;
	}

}
