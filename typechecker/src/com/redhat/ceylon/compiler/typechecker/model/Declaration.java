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
	
    public String getQualifiedName() {
        String cname = getContainer().getQualifiedName();
        return cname.length()==0 ? getName() : cname + "." + getName();
    }

}
