package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ClassOrInterface extends TypeDeclaration {
	
	List<Declaration> members = new ArrayList<Declaration>();

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

}
