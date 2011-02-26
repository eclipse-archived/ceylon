package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class ControlBlock extends Element implements Scope {

	List<Declaration> members = new ArrayList<Declaration>();

	@Override
	public List<Declaration> getMembers() {
		return members;
	}
	
	@Override
	public List<String> getQualifiedName() {
	    return getContainer().getQualifiedName();
	}

}
