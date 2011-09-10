package com.redhat.ceylon.compiler.typechecker.model;

public class DeclarationWithProximity {
    private Declaration declaration;
    private int proximity;
    
    public DeclarationWithProximity(Declaration declaration, int proximity) {
        this.declaration = declaration;
        this.proximity = proximity;
    }
    
    public Declaration getDeclaration() {
        return declaration;
    }
    
    public int getProximity() {
        return proximity;
    }
    
}
