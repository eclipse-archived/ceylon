package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxByte
        implements java.io.Serializable {
    
    private static final long serialVersionUID = 5937898843933134093L;
    
    public byte ref;
    
    public VariableBoxByte() {
        ref = 0;
    }
    
    public VariableBoxByte(byte ref) {
        this.ref = ref;
    }
    
}
