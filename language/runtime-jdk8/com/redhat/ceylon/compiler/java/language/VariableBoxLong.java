package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxLong
        implements java.io.Serializable {
    
    private static final long serialVersionUID = -6894579505815283454L;
    
    public long ref;
    
    public VariableBoxLong() {
        ref = 0L;
    }
    
    public VariableBoxLong(long ref) {
        this.ref = ref;
    }
    
}
