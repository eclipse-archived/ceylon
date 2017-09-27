package org.eclipse.ceylon.model.loader.model;




/**
 * Enumerates the possible Ceylon-generated
 * Java program elements capable of supporting 
 * Java annotations.
 * 
 * @see AnnotationTarget
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
    private final AnnotationTarget target;
    OutputElement(AnnotationTarget target) {
        this.target = target;
    }
    public AnnotationTarget toAnnotationTarget() {
        return target;
    }
}