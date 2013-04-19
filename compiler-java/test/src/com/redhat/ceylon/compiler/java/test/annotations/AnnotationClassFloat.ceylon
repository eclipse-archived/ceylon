import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}
@nomodel
annotation class AnnotationClassFloat(Float f) satisfies SequencedAnnotation<AnnotationClassFloat, Type<Anything>>{}
@nomodel
annotation class AnnotationClassFloatDefaulted(Float f=3.141) satisfies SequencedAnnotation<AnnotationClassFloatDefaulted, Type<Anything>>{}
//@nomodel
//annotation AnnotationClassFloat annotationClassFloat(Float f) => AnnotationClassFloat(f);