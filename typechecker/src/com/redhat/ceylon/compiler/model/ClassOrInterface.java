package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterface extends GenericType implements Scope<Declaration> {
	
	List<Declaration> members = new ArrayList<Declaration>();

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

}
