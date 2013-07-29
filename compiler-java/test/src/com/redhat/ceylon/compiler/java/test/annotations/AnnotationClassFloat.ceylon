import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassFloat(Float f) satisfies SequencedAnnotation<AnnotationClassFloat, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassFloatDefaulted(Float f=3.141) satisfies SequencedAnnotation<AnnotationClassFloatDefaulted, ClassOrInterfaceDeclaration>{}
//@nomodel
//annotation AnnotationClassFloat annotationClassFloat(Float f) => AnnotationClassFloat(f);