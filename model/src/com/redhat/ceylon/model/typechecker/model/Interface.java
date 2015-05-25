package com.redhat.ceylon.model.typechecker.model;

public class Interface extends ClassOrInterface {

    private String javaCompanionClassName;
    private Boolean companionClassNeeded;
    
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

    public Boolean isCompanionClassNeeded() {
        return companionClassNeeded;
    }

    public void setCompanionClassNeeded(Boolean companionClassNeeded) {
        this.companionClassNeeded = companionClassNeeded;
    }
    
    @Override
    public String toString() {
        return "interface " + toStringName();
    }
    
}
