package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public class Package implements Scope {
    
	List<String> name;
	Module module;
	List<Declaration> members = new ArrayList<Declaration>();

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
	public List<Declaration> getMembers() {
		return members;
	}

	@Override
	public Scope getContainer() {
		return null;
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
		return "Package[" + getNameAsString() + "]";
	}
	
	@Override
	public List<String> getQualifiedName() {
	    return getName();
	}
	
	/**
	 * Search only inside the package, ignoring imports
	 */
	//@Override
	public Declaration getMember(/*boolean includeParameters,*/String name) {
        for ( Declaration d: getMembers() ) {
            //if ( !(d instanceof Setter) && (includeParameters || !(d instanceof Parameter)) ) {
            if (!(d instanceof Class && Character.isLowerCase(name.charAt(0)))) { //don't return the type associated with an object dec
                if (d.getName()!=null && d.getName().equals(name)) {
                    return d;
                }
            }
        }
        return null;
    }
	
	@Override
	public ProducedType getDeclaringType(Declaration d) {
	    return null;
	}
    
    /**
     * Search in the package, taking into account 
     * imports
     */
	@Override
    public Declaration getMember(Unit unit, boolean includeParameters, String name) {
        if (unit!=null) {
            //this implements the rule that imports hide 
            //toplevel members of a package
            Declaration d = unit.getImportedDeclaration(name);
            if (d!=null) {
                return d;
            }
        }
        return getMember(/*includeParameters,*/ name);
    }

}
