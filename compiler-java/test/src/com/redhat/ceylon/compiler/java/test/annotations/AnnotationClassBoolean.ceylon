import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}

@nomodel
annotation class AnnotationClassBoolean(Boolean b) satisfies SequencedAnnotation<AnnotationClassBoolean, Type<Anything>>{}
@nomodel
annotation class AnnotationClassBooleanDefaulted(Boolean b=true) satisfies OptionalAnnotation<AnnotationClassBooleanDefaulted, Type<Anything>>{}
@nomodel
annotation class AnnotationClassBooleanVariadic(Boolean* b) satisfies SequencedAnnotation<AnnotationClassBooleanVariadic, Type<Anything>>{}
@nomodel
annotation AnnotationClassBooleanVariadic annotationClassVariadicVariadic(Boolean* b) => AnnotationClassBooleanVariadic(*b);
@nomodel
annotation AnnotationClassBooleanVariadic annotationClassVariadicSequence(Boolean[] b) => AnnotationClassBooleanVariadic(*b);
@nomodel
annotation AnnotationClassBooleanVariadic annotationClassVariadicIterable({Boolean*} b) => AnnotationClassBooleanVariadic(*b);

@nomodel
annotation class AnnotationClassBooleanSequence(Boolean[] b) satisfies SequencedAnnotation<AnnotationClassBooleanSequence, Type<Anything>>{}
@nomodel
annotation AnnotationClassBooleanSequence annotationClassSequenceVariadic(Boolean* b) => AnnotationClassBooleanSequence(b);
@nomodel
annotation AnnotationClassBooleanSequence annotationClassSequenceSequence(Boolean[] b) => AnnotationClassBooleanSequence(b);
//@nomodel
//annotation AnnotationClassBooleanSequence annotationClassSequenceIterable({Boolean*} b) => AnnotationClassBooleanSequence(b.sequence);

@nomodel
annotation class AnnotationClassBooleanIterable({Boolean*} b) satisfies SequencedAnnotation<AnnotationClassBooleanIterable, Type<Anything>>{}
@nomodel
annotation AnnotationClassBooleanIterable annotationClassIterableVariadic(Boolean* b) => AnnotationClassBooleanIterable(b);
@nomodel
annotation AnnotationClassBooleanIterable annotationClassIterableSequence(Boolean[] b) => AnnotationClassBooleanIterable(b);
@nomodel
annotation AnnotationClassBooleanIterable annotationClassIterableIterable({Boolean*} b) => AnnotationClassBooleanIterable(b);
@nomodel
annotationClassVariadicVariadic(true, false)//*
annotationClassVariadicSequence([true, false])//*
annotationClassVariadicIterable({true, false})//*
annotationClassSequenceVariadic(true, false)
annotationClassSequenceSequence([true, false])
////annotationClassSequenceIterable({true, false})
annotationClassIterableVariadic(true, false)
annotationClassIterableSequence([true, false])
annotationClassIterableIterable({true, false})
class AnnotationClassBoolean_callsite(){} 
