package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBoxDouble
        implements java.io.Serializable{
    
    private static final long serialVersionUID = -865429594096963247L;
    
    public double ref;
    
    public VariableBoxDouble() {
        ref = 0.0;
    }
    
    public VariableBoxDouble(double ref) {
        this.ref = ref;
    }
}
