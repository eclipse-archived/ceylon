package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBox<Reference>
        implements java.io.Serializable {

    private static final long serialVersionUID = -7838884087749794052L;
    
    public Reference ref;
    
    public VariableBox() {    
    }
    
    public VariableBox(Reference ref) {
        this.ref = ref;
    }
}
