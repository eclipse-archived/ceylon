package com.redhat.ceylon.compiler.model;

public class TypeParameter extends GenericType {
	
	Boolean covariant;
	Boolean contravariant;
	
	public Boolean isCovariant() {
		return covariant;
	}
	public void setCovariant(Boolean covariant) {
		this.covariant = covariant;
	}
	
	public Boolean isContravariant() {
		return contravariant;
	}
	public void setContravariant(Boolean contravariant) {
		this.contravariant = contravariant;
	}
	
	
}
