@nomodel
annotation class AnnotationTarget(){}

@nomodel
annotation AnnotationTarget annotationTarget() => AnnotationTarget();

@nomodel
annotationTarget
class AnnotationTargetClass(annotationTarget String parameter) {
    
    annotationTarget
    shared Boolean attribute = true;
    
    annotationTarget
    Boolean getter => true;
    
    annotationTarget
    assign getter {}
    
    annotationTarget
    void method(annotationTarget String parameter) {
        annotationTarget
        assert(true);
    }
}

@nomodel
annotationTarget
alias AnnotationTargetAlias => AnnotationTargetClass;

@nomodel
annotationTarget
interface AnnotationTargetInterface {
    annotationTarget
    shared formal void formalMethod(annotationTarget String parameter);
    annotationTarget
    shared default void defaultMethod(annotationTarget String parameter) {}
    annotationTarget
    shared void sharedMethod(annotationTarget String parameter) {}
    annotationTarget
    void method(annotationTarget String parameter) {}
}

@nomodel
annotationTarget
void annotationTargetMethod(annotationTarget String parameter) {
}

@nomodel
annotationTarget
Boolean annotationTargetValue = true;

@nomodel
annotationTarget
object annotationTargetObject {}
