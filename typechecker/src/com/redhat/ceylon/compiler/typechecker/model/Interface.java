package com.redhat.ceylon.compiler.typechecker.model;

public class Interface extends ClassOrInterface {

    @Override
    public boolean isAbstract() {
        return true;
    }
    
}
