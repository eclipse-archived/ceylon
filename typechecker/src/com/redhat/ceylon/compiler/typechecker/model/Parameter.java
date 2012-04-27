package com.redhat.ceylon.compiler.typechecker.model;


public abstract class Parameter extends TypedDeclaration {
    
	private boolean defaulted;
	private boolean sequenced;
    private Declaration declaration;
    private boolean captured = false;
    private boolean unresolvable;
    
    @Override
    public boolean isCaptured() {
        return captured;
    }
    
    public void setCaptured(boolean local) {
        this.captured = local;
    }
    
    public boolean isDefaulted() {
        return defaulted;
    }
    
    public void setDefaulted(boolean defaulted) {
        this.defaulted = defaulted;
    }
    
    public boolean isSequenced() {
        return sequenced;
    }
    
    public void setSequenced(boolean sequenced) {
        this.sequenced = sequenced;
    }
    
    public Declaration getDeclaration() {
        return declaration;
    }
    
    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }
    
    public boolean isUnresolvable() {
        return unresolvable;
    }
    
    public void setUnresolvable(boolean unresolvable) {
        this.unresolvable = unresolvable;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.PARAMETER;
    }

    @Override
    public String toString() {
        return super.toString().replace("[", 
        		"[" + declaration.getName() + "#");
    }
    
}
