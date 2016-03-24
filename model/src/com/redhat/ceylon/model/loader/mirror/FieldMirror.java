package com.redhat.ceylon.model.loader.mirror;


/**
 * Represents a field.
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface FieldMirror extends AnnotatedMirror, AccessibleMirror {

    /**
     * Returns true if this field is static
     */
    boolean isStatic();

    /**
     * Returns true if this field is final
     */
    boolean isFinal();

    /**
     * Returns the type of this field 
     */
    TypeMirror getType();
}
