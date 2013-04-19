import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}
@noanno
annotation class AnnotationClassParam(String name) {}
@noanno
annotation AnnotationClassParam annotationClassParam(String name) => AnnotationClassParam(name);
@noanno
annotation class AnnotationClassAnnotationClass(AnnotationClassParam param) satisfies SequencedAnnotation<AnnotationClassAnnotationClass, Type<Anything>>{}
@noanno
annotation class AnnotationClassAnnotationClassSequence(AnnotationClassParam[] params) satisfies SequencedAnnotation<AnnotationClassAnnotationClassSequence, Type<Anything>>{}
@noanno
annotation AnnotationClassAnnotationClassSequence annotationClassAnnotationClassSequence(AnnotationClassParam[] params) => AnnotationClassAnnotationClassSequence(params);
@noanno
annotation class AnnotationClassAnnotationClassDefaulted(AnnotationClassParam a=AnnotationClassParam("defaulted")) {}
@noanno
annotation AnnotationClassAnnotationClass annotationClassAnnotationClass(AnnotationClassParam a) => AnnotationClassAnnotationClass(a);
@noanno
annotationClassAnnotationClass(AnnotationClassParam("class"))
annotationClassAnnotationClass(annotationClassParam("constructor"))
annotationClassAnnotationClassSequence([annotationClassParam("constructor"), AnnotationClassParam("class")])
class AnnotationClassAnnotationClass_callsite() {}