import ceylon.language.model{SequencedAnnotation, OptionalAnnotation}
import ceylon.language.model.declaration { ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassFloat(Float f) satisfies SequencedAnnotation<AnnotationClassFloat, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassFloatDefaulted(Float f=3.141) satisfies SequencedAnnotation<AnnotationClassFloatDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassFloat annotationClassFloat(Float f=1.0) => AnnotationClassFloat(f);