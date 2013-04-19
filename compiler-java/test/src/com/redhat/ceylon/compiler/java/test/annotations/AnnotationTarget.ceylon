annotation class AnnotationTarget(){}
annotation AnnotationTarget annotationTarget() => AnnotationTarget();

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

annotationTarget
alias AnnotationTargetAlias => AnnotationTargetClass;


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

annotationTarget
void annotationTargetMethod(annotationTarget String parameter) {
}

annotationTarget
Boolean annotationTargetValue = true;

annotationTarget
object annotationTargetObject {}
