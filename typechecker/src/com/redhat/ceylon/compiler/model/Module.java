package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Module {
	
	String name;
	List<Package> packages = new ArrayList<Package>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Package> getMembers() {
		return packages;
	}
	
}
