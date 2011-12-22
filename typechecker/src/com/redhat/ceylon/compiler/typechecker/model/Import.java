package com.redhat.ceylon.compiler.typechecker.model;

public class Import {
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
    
    @Override
    public String toString() {
        return "Import[" + alias + "]";
    }

}
