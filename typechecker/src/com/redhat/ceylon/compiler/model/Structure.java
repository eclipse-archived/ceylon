package com.redhat.ceylon.compiler.model;

public class Structure extends Model {
	
	Scope<Structure> container;
	CompilationUnit compilationUnit;
	
	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
	public void setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public Scope<Structure> getContainer() {
		return container;
	}
	public void setContainer(Scope<Structure> scope) {
		this.container = scope;
	}
	
}
