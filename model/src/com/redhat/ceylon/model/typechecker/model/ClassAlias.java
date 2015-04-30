package com.redhat.ceylon.model.typechecker.model;

public class ClassAlias extends Class {
    
    private TypeDeclaration constructor;
    
    public TypeDeclaration getConstructor() {
        return constructor;
    }
    
    public void setConstructor(TypeDeclaration constructor) {
        this.constructor = constructor;
    }
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
}
