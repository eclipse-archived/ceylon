package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxBoolean
        implements java.io.Serializable {
    
    private static final long serialVersionUID = 6775651874196114832L;
    
    public boolean ref;
    
    public VariableBoxBoolean() {
        ref = false;
    }
    
    public VariableBoxBoolean(boolean ref) {
        this.ref = ref;
    }
    
}
