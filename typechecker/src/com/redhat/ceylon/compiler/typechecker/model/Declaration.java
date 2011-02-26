package com.redhat.ceylon.compiler.typechecker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a named, annotated program element:
 * a class, interface, type parameter, parameter, 
 * method, local, or attribute.
 * 
 * @author Gavin King
 *
 */
public abstract class Declaration extends Element {
	
	String name;
	boolean shared;
	boolean formal;
	boolean actual;
	boolean def;
	List<Annotation> annotations = new ArrayList<Annotation>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isShared() {
		return shared;
	}
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	public List<Annotation> getAnnotations() {
		return annotations;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + 
			"[" + name + "]";
	}
	
    public List<String> getQualifiedName() {
        return Util.list( getContainer().getQualifiedName(), getName() );
    }
    
    public boolean isActual() {
        return actual;
    }
    
    public void setActual(boolean actual) {
        this.actual = actual;
    }
    
    public boolean isFormal() {
        return formal;
    }
    
    public void setFormal(boolean formal) {
        this.formal = formal;
    }
    
    public boolean isDefault() {
        return def;
    }
    
    public void setDefault(boolean def) {
        this.def = def;
    }

}
