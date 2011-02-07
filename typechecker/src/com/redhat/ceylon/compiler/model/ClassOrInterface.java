package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterface extends GenericType implements Scope<Structure> {
	
	List<Structure> members = new ArrayList<Structure>();

	@Override
	public List<Structure> getMembers() {
		return members;
	}

}
