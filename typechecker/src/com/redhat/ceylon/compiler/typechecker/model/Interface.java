package com.redhat.ceylon.compiler.typechecker.model;

public class Interface extends ClassOrInterface {

    private String javaCompanionClassName;

    @Override
    public boolean isAbstract() {
        return true;
    }

    public void setJavaCompanionClassName(String name) {
        this.javaCompanionClassName = name;
    }
    
    public String getJavaCompanionClassName() {
        return javaCompanionClassName;
    }
}
