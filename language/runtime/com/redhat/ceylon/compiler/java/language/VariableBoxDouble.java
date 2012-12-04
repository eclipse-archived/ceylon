package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxDouble {
    
    public double ref;
    
    public VariableBoxDouble() {
        ref = 0.0;
    }
    
    public VariableBoxDouble(double ref) {
        this.ref = ref;
    }
}
