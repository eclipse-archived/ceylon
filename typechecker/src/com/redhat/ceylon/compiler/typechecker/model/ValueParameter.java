package com.redhat.ceylon.compiler.typechecker.model;

public class ValueParameter extends Parameter {
    
    private boolean hidden;
    
    public boolean isHidden() {
        return hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
}
