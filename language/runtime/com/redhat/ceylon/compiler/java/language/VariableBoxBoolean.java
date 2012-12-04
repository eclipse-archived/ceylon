package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxBoolean {
    
    public boolean ref;
    
    public VariableBoxBoolean() {
        ref = false;
    }
    
    public VariableBoxBoolean(boolean ref) {
        this.ref = ref;
    }
    
}
