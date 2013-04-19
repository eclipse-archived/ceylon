import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}
@noanno
annotation class AnnotationClassFloat(Float f) satisfies SequencedAnnotation<AnnotationClassFloat, Type<Anything>>{}
@noanno
annotation class AnnotationClassFloatDefaulted(Float f=3.141) satisfies SequencedAnnotation<AnnotationClassFloatDefaulted, Type<Anything>>{}
//@noanno
//annotation AnnotationClassFloat annotationClassFloat(Float f) => AnnotationClassFloat(f);