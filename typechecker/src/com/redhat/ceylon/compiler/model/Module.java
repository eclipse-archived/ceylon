package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Module {
	
	String name;
	List<Package> packages = new ArrayList<Package>();
	List<Module> dependencies = new ArrayList<Module>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Package> getPackages() {
		return packages;
	}
	
	public List<Module> getDependencies() {
		return dependencies;
	}
	
}
