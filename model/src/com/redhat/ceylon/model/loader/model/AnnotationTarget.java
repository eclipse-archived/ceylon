package com.redhat.ceylon.model.loader.model;

import java.util.EnumSet;

import com.redhat.ceylon.model.typechecker.model.Class;


/**
 * Mirrors the elements of {@link java.lang.annotation.ElementType} for 
 * the purpose of targeting java annotations.
 */
public enum AnnotationTarget {

    TYPE,
    FIELD,
    METHOD,
    PARAMETER,
    CONSTRUCTOR,
    LOCAL_VARIABLE,
    ANNOTATION_TYPE,
    PACKAGE,
    TYPE_USE,
    TYPE_PARAMETER;
    
    /**
     * Returns the possible targets of the given annotation proxy class, 
     * or null if the given class is not an annotation proxy.
     */
    public static EnumSet<AnnotationTarget> annotationTargets(Class annotationClass) {
        if (annotationClass instanceof AnnotationProxyClass) {
            return ((AnnotationProxyClass)annotationClass).getAnnotationTarget();
        } else {
            return null;
        }
    }
}
