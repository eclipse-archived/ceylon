package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxByte {
    
    public byte ref;
    
    public VariableBoxByte() {
        ref = 0;
    }
    
    public VariableBoxByte(byte ref) {
        this.ref = ref;
    }
    
}
