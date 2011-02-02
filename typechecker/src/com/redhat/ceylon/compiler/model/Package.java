package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Package implements Scope<Declaration> {
	String name;
	Module module;
	List<Declaration> members = new ArrayList<Declaration>();

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<Declaration> getMembers() {
		return members;
	}
}
