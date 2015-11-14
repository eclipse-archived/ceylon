package com.redhat.ceylon.model.loader.mirror;

/**
 * Represents an annotation
 *
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface AnnotationMirror {

    /**
     * Returns the annotation value of the given annotation field. The value should be wrapped as such:
     * 
     * - String for a string value
     * - boxed value for a primitive value (Integer, Character…)
     * - TypeMirror for a class value
     * - AnnotationMirror for an annotation value
     * - List for an array (the array elements must be wrapped using the same rules)
     */
    Object getValue(String fieldName);

    /**
     * Returns the value of the "value" field
     */
    Object getValue();

}
