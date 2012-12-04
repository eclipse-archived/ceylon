package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxInt {
    
    public int ref;
    
    public VariableBoxInt() {
        ref = 0;
    }
    
    public VariableBoxInt(int ref) {
        this.ref = ref;
    }
    
}
