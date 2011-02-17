package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class ControlBlock extends Element implements Scope {

	List<Declaration> members = new ArrayList<Declaration>();

	@Override
	public List<Declaration> getMembers() {
		return members;
	}

}
