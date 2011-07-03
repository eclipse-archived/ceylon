package com.redhat.ceylon.compiler.typechecker.model;

public class Import {
	String alias;
	Declaration declaration;
	
	public Declaration getDeclaration() {
		return declaration;
	}
	public void setDeclaration(Declaration declaration) {
		this.declaration = declaration;
	}
	
	public String getAlias() {
		return alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
