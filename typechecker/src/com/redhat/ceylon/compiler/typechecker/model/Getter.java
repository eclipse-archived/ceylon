package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An attribute getter.
 * 
 * @author Gavin King
 *
 */
public class Getter extends MethodOrValue implements Scope {
	
	List<Declaration> members = new ArrayList<Declaration>();

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

}
