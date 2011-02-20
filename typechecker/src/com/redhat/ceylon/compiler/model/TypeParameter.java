package com.redhat.ceylon.compiler.model;

public class TypeParameter extends TypeDeclaration {
	
	boolean covariant;
	boolean contravariant;
	Declaration declaration;
	
	public boolean isCovariant() {
		return covariant;
	}
	
	public void setCovariant(boolean covariant) {
		this.covariant = covariant;
	}
	
	public boolean isContravariant() {
		return contravariant;
	}
	
	public void setContravariant(boolean contravariant) {
		this.contravariant = contravariant;
	}
	
	public Declaration getDeclaration() {
        return declaration;
    }
	
	public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
	
}
