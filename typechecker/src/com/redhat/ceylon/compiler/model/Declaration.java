package com.redhat.ceylon.compiler.model;

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
public class Declaration extends Structure {
	
	String name;
	Boolean shared;
	List<Annotation> annotations = new ArrayList<Annotation>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean isShared() {
		return shared;
	}
	public void setShared(Boolean shared) {
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
	
}
