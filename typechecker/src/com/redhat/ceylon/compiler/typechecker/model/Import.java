package com.redhat.ceylon.compiler.typechecker.model;

public class Import {
	
	private TypeDeclaration typeDeclaration;
	private String alias;
	private Declaration declaration;
	private boolean wildcardImport;
	private boolean dynamicImport;
	
	public Import() {}

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
    
    public boolean isWildcardImport() {
        return wildcardImport;
    }
    
    public void setWildcardImport(boolean wildcardImport) {
        this.wildcardImport = wildcardImport;
    }
    
    public boolean isDynamicImport() {
        return dynamicImport;
    }
    
    public void setDynamicImport(boolean dynamicImport) {
        this.dynamicImport = dynamicImport;
    }

}
