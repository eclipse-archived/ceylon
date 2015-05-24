package com.redhat.ceylon.model.typechecker.model;

public class InterfaceAlias extends Interface {
    
    @Override
    public boolean isAlias() {
        return true;
    }
    

    @Override
    public Interface getExtendedTypeDeclaration() {
        return (Interface) super.getExtendedTypeDeclaration();
    }
    
}
