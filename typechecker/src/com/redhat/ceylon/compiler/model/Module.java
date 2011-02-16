package com.redhat.ceylon.compiler.model;

import java.util.ArrayList;
import java.util.List;

public class Module {
	
	private List<String> name;
	private List<Package> packages = new ArrayList<Package>();
	private List<Module> dependencies = new ArrayList<Module>();
    private boolean available;

    /**
     * Whether or not the module is available in the
     * source path or the repository
     */
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getName() {
		return name;
	}
	
	public void setName(List<String> name) {
		this.name = name;
	}
	
	public List<Package> getPackages() {
		return packages;
	}
	
	public List<Module> getDependencies() {
		return dependencies;
	}
	
	public List<Package> getAllPackages() {
		List<Package> list = new ArrayList<Package>();
		list.addAll(packages);
		for (Module m: dependencies) {
			list.addAll(m.getPackages());
		}
		return list;
	}
	
    public String getNameAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<name.size(); i++) {
            sb.append(name.get(i));
            if (i<name.size()-1) sb.append('.');
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return "Module[" + getNameAsString() + "]";
    }
    
}
