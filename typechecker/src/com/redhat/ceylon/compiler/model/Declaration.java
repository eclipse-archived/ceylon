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
public class Declaration extends Node {
	
	String name;
	Boolean shared;
	Scope<Declaration> container;
	CompilationUnit compilationUnit;
	List<Annotation> annotations = new ArrayList<Annotation>();
	
	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
	public void setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

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
	
	public Scope<Declaration> getContainer() {
		return container;
	}
	public void setContainer(Scope<Declaration> scope) {
		this.container = scope;
	}
	
	public List<Annotation> getAnnotations() {
		return annotations;
	}
	
}
