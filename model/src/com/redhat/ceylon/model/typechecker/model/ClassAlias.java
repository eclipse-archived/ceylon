package com.redhat.ceylon.model.typechecker.model;

import java.util.List;

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
    
    @Override
    void collectSupertypeDeclarations(
            List<TypeDeclaration> results) {
        Type et = getExtendedType();
        if (et!=null) { 
            et.getDeclaration()
                .collectSupertypeDeclarations(results);
        }
    }
    
    @Override
    public boolean inherits(TypeDeclaration dec) {
        Type et = getExtendedType();
        if (et!=null) {
            return et.getDeclaration().inherits(dec);
        }
        return false;
    }
    
}
