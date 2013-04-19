import ceylon.language.metamodel{OptionalAnnotation, SequencedAnnotation, Type}

@noanno
annotation class AnnotationClassNullary() satisfies OptionalAnnotation<AnnotationClassNullary, Type<Anything>>{}
@noanno
annotation class AnnotationClassNullarySequenced() satisfies SequencedAnnotation<AnnotationClassNullarySequenced, Type<Anything>>{}
@noanno
annotation AnnotationClassNullary annotationClassNullary() => AnnotationClassNullary();
@noanno
annotation AnnotationClassNullarySequenced annotationClassNullarySequenced() => AnnotationClassNullarySequenced();
@noanno
annotationClassNullary
annotationClassNullarySequenced
class AnnotationClassNullary_callsite() {}
