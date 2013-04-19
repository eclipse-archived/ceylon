import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}

@noanno
annotation class AnnotationClassBoolean(Boolean b) satisfies SequencedAnnotation<AnnotationClassBoolean, Type<Anything>>{}
@noanno
annotation AnnotationClassBoolean annotationClassBoolean(Boolean x = true) => AnnotationClassBoolean(x);
@noanno
annotation class AnnotationClassBooleanDefaulted(Boolean b=true) satisfies OptionalAnnotation<AnnotationClassBooleanDefaulted, Type<Anything>>{}
@noanno
annotation class AnnotationClassBooleanVariadic(Boolean* b) satisfies SequencedAnnotation<AnnotationClassBooleanVariadic, Type<Anything>>{}
@noanno
annotation AnnotationClassBooleanVariadic annotationClassVariadicVariadic(Boolean* b) => AnnotationClassBooleanVariadic(*b);
@noanno
annotation AnnotationClassBooleanVariadic annotationClassVariadicSequence(Boolean[] b) => AnnotationClassBooleanVariadic(*b);
@noanno
annotation AnnotationClassBooleanVariadic annotationClassVariadicIterable({Boolean*} b) => AnnotationClassBooleanVariadic(*b);

@noanno
annotation class AnnotationClassBooleanSequence(Boolean[] b) satisfies SequencedAnnotation<AnnotationClassBooleanSequence, Type<Anything>>{}
@noanno
annotation AnnotationClassBooleanSequence annotationClassSequenceVariadic(Boolean* b) => AnnotationClassBooleanSequence(b);
@noanno
annotation AnnotationClassBooleanSequence annotationClassSequenceSequence(Boolean[] b) => AnnotationClassBooleanSequence(b);
//@noanno
//annotation AnnotationClassBooleanSequence annotationClassSequenceIterable({Boolean*} b) => AnnotationClassBooleanSequence(b.sequence);

@noanno
annotation class AnnotationClassBooleanIterable({Boolean*} b) satisfies SequencedAnnotation<AnnotationClassBooleanIterable, Type<Anything>>{}
@noanno
annotation AnnotationClassBooleanIterable annotationClassIterableVariadic(Boolean* b) => AnnotationClassBooleanIterable(b);
@noanno
annotation AnnotationClassBooleanIterable annotationClassIterableSequence(Boolean[] b) => AnnotationClassBooleanIterable(b);
@noanno
annotation AnnotationClassBooleanIterable annotationClassIterableIterable({Boolean*} b) => AnnotationClassBooleanIterable(b);
@noanno
annotationClassBoolean()
annotationClassBoolean{}
annotationClassVariadicVariadic()//*
annotationClassVariadicVariadic(true)//*
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
