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
	
	@Override
	public String toString() {
        StringBuilder string = new StringBuilder("Module[");
        for(String node : name) {
            string.append(node).append('.');
        }
        if ( name.size() != 0 ) {
            string.deleteCharAt( string.length() - 1 );
        }
        string.append("]");
		return string.toString();
	}
	
}
