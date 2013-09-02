package com.redhat.ceylon.compiler.typechecker.model;


public class NothingType extends TypeDeclaration {
    
    public NothingType(Unit unit) {
        this.unit = unit;
    }
    
    @Override
    public void addMember(Declaration declaration) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getName() {
        return "Nothing";
    }
    
    @Override
    public Scope getContainer() {
    	return unit.getAnythingDeclaration().getContainer();
    }
    
    @Override
    public String getQualifiedNameString() {
        return "ceylon.language::Nothing";
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
    
    @Override
    public boolean isShared() {
        return true;
    }
    
    @Override
    public DeclarationKind getDeclarationKind() {
        return DeclarationKind.TYPE;
    }
    
    @Override
    public boolean equals(Object object) {
    	return object instanceof NothingType;
    }
    
}
