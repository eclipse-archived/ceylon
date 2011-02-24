package com.redhat.ceylon.compiler.typechecker.model;

public class ValueParameter extends Parameter {
    
    boolean captured = true;
    
    public boolean isCaptured() {
        return captured;
    }
    
    public void setCaptured(boolean local) {
        this.captured = local;
    }
    
}
