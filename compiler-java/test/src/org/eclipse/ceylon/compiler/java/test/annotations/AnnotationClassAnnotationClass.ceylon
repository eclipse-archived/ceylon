import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassParam(String name) satisfies OptionalAnnotation<AnnotationClassParam, ClassOrInterfaceDeclaration> {}
@nomodel
annotation AnnotationClassParam annotationClassParam(String name) => AnnotationClassParam(name);
@nomodel
annotation final class AnnotationClassAnnotationClass(AnnotationClassParam param) satisfies SequencedAnnotation<AnnotationClassAnnotationClass, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassAnnotationClassSequence(AnnotationClassParam[] params) satisfies SequencedAnnotation<AnnotationClassAnnotationClassSequence, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassAnnotationClassSequence annotationClassAnnotationClassSequence(AnnotationClassParam[] params) => AnnotationClassAnnotationClassSequence(params);
@nomodel
annotation final class AnnotationClassAnnotationClassDefaulted(AnnotationClassParam a=AnnotationClassParam("defaulted")) satisfies OptionalAnnotation<AnnotationClassAnnotationClassDefaulted, ClassOrInterfaceDeclaration> {}
@nomodel
annotation AnnotationClassAnnotationClass annotationClassAnnotationClass(AnnotationClassParam a) => AnnotationClassAnnotationClass(a);
@nomodel
annotationClassAnnotationClass(AnnotationClassParam("class"))
annotationClassAnnotationClass(annotationClassParam("constructor"))
annotationClassAnnotationClassSequence([annotationClassParam("constructor"), AnnotationClassParam("class")])
class AnnotationClassAnnotationClass_callsite() {}