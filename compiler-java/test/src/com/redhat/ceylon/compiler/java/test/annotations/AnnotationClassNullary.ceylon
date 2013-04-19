import ceylon.language.metamodel{OptionalAnnotation, SequencedAnnotation, Type}

@nomodel
annotation class AnnotationClassNullary() satisfies OptionalAnnotation<AnnotationClassNullary, Type<Anything>>{}
@nomodel
annotation class AnnotationClassNullarySequenced() satisfies SequencedAnnotation<AnnotationClassNullarySequenced, Type<Anything>>{}
@nomodel
annotation AnnotationClassNullary annotationClassNullary() => AnnotationClassNullary();
@nomodel
annotation AnnotationClassNullarySequenced annotationClassNullarySequenced() => AnnotationClassNullarySequenced();
@nomodel
annotationClassNullary
annotationClassNullarySequenced
class AnnotationClassNullary_callsite() {}
