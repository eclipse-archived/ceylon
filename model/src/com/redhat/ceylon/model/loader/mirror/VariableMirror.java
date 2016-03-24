package com.redhat.ceylon.model.loader.mirror;

/**
 * Represents a method/constructor parameter.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface VariableMirror extends AnnotatedMirror {
    
    /**
     * Returns this parameter's type
     */
    TypeMirror getType();
}
