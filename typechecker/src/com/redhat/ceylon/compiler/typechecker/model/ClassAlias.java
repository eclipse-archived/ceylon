package com.redhat.ceylon.compiler.typechecker.model;

public class ClassAlias extends Class {
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
}
