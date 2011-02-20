package com.redhat.ceylon.compiler.model;

public class Import {
	String alias;
	Declaration declaration;
	boolean implicit;
	
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
	
	public boolean isImplicit() {
		return implicit;
	}
	
	public void setImplicit(boolean implicit) {
		this.implicit = implicit;
	}
	
}
