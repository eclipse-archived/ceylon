package com.redhat.ceylon.model.loader.model;

import java.util.EnumSet;


/**
 * Enumerates the possible program elements capable of supporting 
 * Java annotations.
 */
public enum OutputElement {
    TYPE(AnnotationTarget.TYPE),
    FIELD(AnnotationTarget.FIELD),
    METHOD(AnnotationTarget.METHOD),
    GETTER(AnnotationTarget.METHOD),
    SETTER(AnnotationTarget.METHOD),
    PARAMETER(AnnotationTarget.PARAMETER),
    CONSTRUCTOR(AnnotationTarget.CONSTRUCTOR),
    LOCAL_VARIABLE(AnnotationTarget.LOCAL_VARIABLE),
    ANNOTATION_TYPE(AnnotationTarget.ANNOTATION_TYPE),
    PACKAGE(AnnotationTarget.PACKAGE);
    
    private final AnnotationTarget annotationTarget;

    private OutputElement(AnnotationTarget annotationTarget) {
        this.annotationTarget = annotationTarget;
    }
    
    public AnnotationTarget getAnnotationTarget() {
        return annotationTarget;
    }
    
    public static EnumSet<OutputElement> possibleCeylonTargets(EnumSet<AnnotationTarget> targets) {
        if (targets == null) {
            targets = EnumSet.allOf(AnnotationTarget.class);
        }
        EnumSet<OutputElement> result = EnumSet.noneOf(OutputElement.class);
        for (AnnotationTarget t : targets) {
            for (OutputElement oe : OutputElement.values()) {
                if (oe.getAnnotationTarget() == t) {
                    result.add(oe);
                }
            }
        }
        return result;
    }
}