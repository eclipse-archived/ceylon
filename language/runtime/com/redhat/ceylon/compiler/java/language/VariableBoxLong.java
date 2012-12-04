package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxLong {
    
    public long ref;
    
    public VariableBoxLong() {
        ref = 0L;
    }
    
    public VariableBoxLong(long ref) {
        this.ref = ref;
    }
    
}
