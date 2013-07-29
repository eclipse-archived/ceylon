import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassParam(String name) {}
@nomodel
annotation AnnotationClassParam annotationClassParam(String name) => AnnotationClassParam(name);
@nomodel
annotation class AnnotationClassAnnotationClass(AnnotationClassParam param) satisfies SequencedAnnotation<AnnotationClassAnnotationClass, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassAnnotationClassSequence(AnnotationClassParam[] params) satisfies SequencedAnnotation<AnnotationClassAnnotationClassSequence, ClassOrInterfaceDeclaration>{}
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