package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Package implements Scope {
	List<String> name;
	Module module;
	List<Structure> members = new ArrayList<Structure>();

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<String> getName() {
		return name;
	}

	public void setName(List<String> name) {
		this.name = name;
	}

	@Override
	public List<Structure> getMembers() {
		return members;
	}

	@Override
	public Scope getContainer() {
		return null;
	}
	
	@Override
	public String toString() {
		return "Package[" + name + "]";
	}
	
}
