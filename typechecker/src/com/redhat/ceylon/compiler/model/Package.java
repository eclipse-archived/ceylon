package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Package implements Scope<Structure> {
	String name;
	Module module;
	List<Structure> members = new ArrayList<Structure>();

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
	public List<Structure> getMembers() {
		return members;
	}

	@Override
	public Scope<Structure> getContainer() {
		return null;
	}
	
}
