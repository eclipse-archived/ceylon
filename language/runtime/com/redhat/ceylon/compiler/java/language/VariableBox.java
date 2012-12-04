package com.redhat.ceylon.compiler.java.language;

/**
 * Used for captured {@code variable} local attributes. 
 */
public class VariableBox<Reference> {

    public Reference ref;
    
    public VariableBox() {    
    }
    
    public VariableBox(Reference ref) {
        this.ref = ref;
    }
}
