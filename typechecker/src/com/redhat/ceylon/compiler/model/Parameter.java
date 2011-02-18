package com.redhat.ceylon.compiler.model;

public abstract class Parameter extends TypedDeclaration {
    
    boolean defaulted;
    boolean sequenced;
    
    public boolean isDefaulted() {
        return defaulted;
    }
    
    public void setDefaulted(boolean defaulted) {
        this.defaulted = defaulted;
    }
    
    public boolean isSequenced() {
        return sequenced;
    }
    
    public void setSequenced(boolean sequenced) {
        this.sequenced = sequenced;
    }
    
}
