import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassBoolean(Boolean b) satisfies SequencedAnnotation<AnnotationClassBoolean, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassBoolean annotationClassBoolean(Boolean x = true) => AnnotationClassBoolean(x);
@nomodel
annotation final class AnnotationClassBooleanDefaulted(Boolean b=true) satisfies OptionalAnnotation<AnnotationClassBooleanDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassBooleanVariadic(Boolean* b) satisfies SequencedAnnotation<AnnotationClassBooleanVariadic, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassBooleanVariadic annotationClassVariadicVariadic(Boolean* b) => AnnotationClassBooleanVariadic(*b);
@nomodel
annotation AnnotationClassBooleanVariadic annotationClassVariadicSequence(Boolean[] b) => AnnotationClassBooleanVariadic(*b);
@nomodel
annotation AnnotationClassBooleanVariadic annotationClassVariadicIterable({Boolean*} b) => AnnotationClassBooleanVariadic(*b);

@nomodel
annotation final class AnnotationClassBooleanSequence(Boolean[] b) satisfies SequencedAnnotation<AnnotationClassBooleanSequence, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassBooleanSequence annotationClassSequenceVariadic(Boolean* b) => AnnotationClassBooleanSequence(b);
@nomodel
annotation AnnotationClassBooleanSequence annotationClassSequenceSequence(Boolean[] b) => AnnotationClassBooleanSequence(b);
//@nomodel
//annotation AnnotationClassBooleanSequence annotationClassSequenceIterable({Boolean*} b) => AnnotationClassBooleanSequence(b.sequence);

@nomodel
annotation final class AnnotationClassBooleanIterable({Boolean*} b) satisfies SequencedAnnotation<AnnotationClassBooleanIterable, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassBooleanIterable annotationClassIterableVariadic(Boolean* b) => AnnotationClassBooleanIterable(b);
@nomodel
annotation AnnotationClassBooleanIterable annotationClassIterableSequence(Boolean[] b) => AnnotationClassBooleanIterable(b);
@nomodel
annotation AnnotationClassBooleanIterable annotationClassIterableIterable({Boolean*} b) => AnnotationClassBooleanIterable(b);
@nomodel
annotationClassBoolean()
annotationClassBoolean{}
annotationClassVariadicVariadic()//*
annotationClassVariadicVariadic(true)//*
annotationClassVariadicVariadic(true, false)//*
annotationClassVariadicSequence([true, false])//*
annotationClassVariadicIterable({true, false})//*
annotationClassSequenceVariadic(true, false)
annotationClassSequenceVariadic()
annotationClassSequenceSequence([true, false])
////annotationClassSequenceIterable({true, false})
annotationClassIterableVariadic(true, false)
annotationClassIterableVariadic()
annotationClassIterableSequence([true, false])
annotationClassIterableIterable({true, false})
class AnnotationClassBoolean_callsite(){} 
