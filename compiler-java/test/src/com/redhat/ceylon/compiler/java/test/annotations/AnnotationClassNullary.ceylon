import ceylon.language.metamodel{OptionalAnnotation, SequencedAnnotation}
import ceylon.language.metamodel.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassNullary() satisfies OptionalAnnotation<AnnotationClassNullary, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassNullarySequenced() satisfies SequencedAnnotation<AnnotationClassNullarySequenced, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassNullary annotationClassNullary() => AnnotationClassNullary();
@nomodel
annotation AnnotationClassNullarySequenced annotationClassNullarySequenced() => AnnotationClassNullarySequenced();
@nomodel
annotationClassNullary
annotationClassNullarySequenced
class AnnotationClassNullary_callsite() {}
