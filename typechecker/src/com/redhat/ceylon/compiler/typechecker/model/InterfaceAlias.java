package com.redhat.ceylon.compiler.typechecker.model;

public class InterfaceAlias extends Interface {
    
    @Override
    public boolean isAlias() {
        return true;
    }
    
}
