package com.redhat.ceylon.compiler.typechecker.model;

public class Import {
	
	private TypeDeclaration typeDeclaration;
	private String alias;
	private Declaration declaration;

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
    
    public TypeDeclaration getTypeDeclaration() {
		return typeDeclaration;
	}
    
    public void setTypeDeclaration(TypeDeclaration typeDeclaration) {
		this.typeDeclaration = typeDeclaration;
	}
    
    @Override
    public String toString() {
        return "Import[" + alias + "]";
    }

}
