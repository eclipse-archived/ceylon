import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}
@nomodel
annotation class AnnotationClassParam(String name) {}
@nomodel
annotation AnnotationClassParam annotationClassParam(String name) => AnnotationClassParam(name);
@nomodel
annotation class AnnotationClassAnnotationClass(AnnotationClassParam param) satisfies SequencedAnnotation<AnnotationClassAnnotationClass, Type<Anything>>{}
@nomodel
annotation class AnnotationClassAnnotationClassSequence(AnnotationClassParam[] params) satisfies SequencedAnnotation<AnnotationClassAnnotationClassSequence, Type<Anything>>{}
@nomodel
annotation AnnotationClassAnnotationClassSequence annotationClassAnnotationClassSequence(AnnotationClassParam[] params) => AnnotationClassAnnotationClassSequence(params);
@nomodel
annotation class AnnotationClassAnnotationClassDefaulted(AnnotationClassParam a=AnnotationClassParam("defaulted")) {}
@nomodel
annotation AnnotationClassAnnotationClass annotationClassAnnotationClass(AnnotationClassParam a) => AnnotationClassAnnotationClass(a);
@nomodel
annotationClassAnnotationClass(AnnotationClassParam("class"))
annotationClassAnnotationClass(annotationClassParam("constructor"))
annotationClassAnnotationClassSequence([annotationClassParam("constructor"), AnnotationClassParam("class")])
class AnnotationClassAnnotationClass_callsite() {}