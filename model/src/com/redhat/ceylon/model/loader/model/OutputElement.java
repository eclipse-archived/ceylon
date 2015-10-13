package com.redhat.ceylon.model.loader.model;




/**
 * Enumerates the possible Ceylon-generated
 * Java program elements capable of supporting 
 * Java annotations.
 * 
 * @see AnnotationTarget
 */
public enum OutputElement {
    TYPE,
    FIELD,
    METHOD,
    GETTER,
    SETTER,
    PARAMETER,
    CONSTRUCTOR,
    LOCAL_VARIABLE,
    ANNOTATION_TYPE,
    PACKAGE;
}