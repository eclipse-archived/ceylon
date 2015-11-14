package com.redhat.ceylon.model.loader.mirror;

/**
 * Represents an annotated program element (class, method, constructor, field, parameter)
 * 
 * @author Stéphane Épardaud <stef@epardaud.fr>
 */
public interface AnnotatedMirror {

    /**
     * Returns the program element's name
     */
    String getName();

    /**
     * Gets an annotation by annotation type name (fully qualified annotation class name)
     */
    AnnotationMirror getAnnotation(String type);
}
