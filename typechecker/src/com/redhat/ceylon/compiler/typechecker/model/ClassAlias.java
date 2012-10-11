package com.redhat.ceylon.compiler.typechecker.model;

public class ClassAlias extends Class {
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
    @Override
    public boolean isAbstract() {
        Class etd = getExtendedTypeDeclaration();
        return etd!=null && etd!=this && etd.isAbstract();
    }
    
}
