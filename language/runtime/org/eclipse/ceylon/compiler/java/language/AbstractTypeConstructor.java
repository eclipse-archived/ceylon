package org.eclipse.ceylon.compiler.java.language;

import org.eclipse.ceylon.compiler.java.runtime.model.TypeDescriptor;

/**
 * Baseclass for all type constructors.
 */
public abstract class AbstractTypeConstructor {
    
    private final String string;

    public AbstractTypeConstructor(String string) {
        this.string = string;
    }
    
    /**
     * Apply the given types to this type constructor
     * @param types
     * @return
     */
    public abstract Object apply(TypeDescriptor[] types);
    
    public String toString() {
        return string;
    }
}