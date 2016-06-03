package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxInt
        implements java.io.Serializable {
    
    private static final long serialVersionUID = -8191517797249618409L;
    
    public int ref;
    
    public VariableBoxInt() {
        ref = 0;
    }
    
    public VariableBoxInt(int ref) {
        this.ref = ref;
    }
    
}
