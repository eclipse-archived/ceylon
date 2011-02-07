package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class ControlBlock extends Structure implements Scope {

	List<Structure> members = new ArrayList<Structure>();

	@Override
	public List<Structure> getMembers() {
		return members;
	}

}
