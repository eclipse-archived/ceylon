package com.redhat.ceylon.compiler.model;

public class Structure extends Model {
	
	Scope container;
	CompilationUnit compilationUnit;
	
	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}
	public void setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public Scope getContainer() {
		return container;
	}
	
	public void setContainer(Scope scope) {
		this.container = scope;
	}
	
}
