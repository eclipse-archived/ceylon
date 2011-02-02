package com.redhat.ceylon.compiler.model;

public class Import {
	String alias;
	Declaration declaration;
	Boolean implicit;
	
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
	
	public Boolean isImplicit() {
		return implicit;
	}
	
	public void setImplicit(Boolean implicit) {
		this.implicit = implicit;
	}
	
}
