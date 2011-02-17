package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassOrInterface extends TypeDeclaration implements Scope {
	
	List<Declaration> members = new ArrayList<Declaration>();

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

}
