package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Element {
	
	Scope container;
	Unit unit;
    List<Declaration> members = new ArrayList<Declaration>();
    	
	public Unit getUnit() {
		return unit;
	}
	
	public void setUnit(Unit compilationUnit) {
		this.unit = compilationUnit;
	}

	public Scope getContainer() {
		return container;
	}
	
	public void setContainer(Scope scope) {
		this.container = scope;
	}
	
    public List<Declaration> getMembers() {
        return members;
    }
    
    public List<String> getQualifiedName() {
        return getContainer().getQualifiedName();
    }
    
    /**
     * Search only directly inside the given scope,
     * without considering containing scopes or 
     * imports. 
     */
    public Declaration getMember(boolean includeParameters, String name) {
        for ( Declaration d: getMembers() ) {
            if ( !(d instanceof Setter) && (includeParameters || !(d instanceof Parameter)) ) {
                if (d.getName()!=null && d.getName().equals(name)) {
                    return d;
                }
            }
        }
        return null;
    }
    
    public ProducedType getDeclaringType(Declaration d) {
        return getContainer().getDeclaringType(d);
    }

    /**
     * Search in the given scope, taking into account 
     * containing scopes and imports
     */
    public Declaration getMember(Unit unit, boolean includeParameters, String name) {
        Declaration d = getMember(includeParameters, name);
        if (d!=null) {
            return d;
        }
        else if (getContainer()!=null) {
            return getContainer().getMember(unit, includeParameters, name);
        }
        else {
            //union type or bottom type 
            return null;
        }
    }

}
