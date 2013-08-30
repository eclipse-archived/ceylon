import ceylon.language.model{SequencedAnnotation, OptionalAnnotation, Annotation}
import ceylon.language.model.declaration { Declaration, ClassOrInterfaceDeclaration }

@nomodel
annotation final class AnnotationClassMetamodelReference(Declaration mmr) satisfies SequencedAnnotation<AnnotationClassMetamodelReference, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassMetamodelReferenceDefaulted(Declaration mmr=`class AnnotationClassMetamodelReference`) satisfies SequencedAnnotation<AnnotationClassMetamodelReferenceDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation final class AnnotationClassMetamodelReferenceVariadic(Declaration* mmrs) satisfies SequencedAnnotation<AnnotationClassMetamodelReferenceVariadic, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassMetamodelReference annotationClassMetamodelReference(Declaration mmr) => AnnotationClassMetamodelReference(mmr);
@nomodel
annotation AnnotationClassMetamodelReference annotationClassMetamodelReferenceLiteral() => AnnotationClassMetamodelReference(`class AnnotationClassMetamodelReference`);
@nomodel
annotation AnnotationClassMetamodelReferenceDefaulted annotationClassMetamodelReferenceDefaulted1(Declaration mmr) => AnnotationClassMetamodelReferenceDefaulted(mmr);
@nomodel
annotation AnnotationClassMetamodelReferenceDefaulted annotationClassMetamodelReferenceDefaulted2() => AnnotationClassMetamodelReferenceDefaulted();
@nomodel
annotation AnnotationClassMetamodelReferenceVariadic annotationClassMetamodelReferenceVariadic1(Declaration* mmr) => AnnotationClassMetamodelReferenceVariadic(*mmr);
@nomodel
annotation AnnotationClassMetamodelReferenceVariadic annotationClassMetamodelReferenceVariadic2() => AnnotationClassMetamodelReferenceVariadic();
@nomodel
annotation AnnotationClassMetamodelReferenceVariadic annotationClassMetamodelReferenceVariadic3(Declaration mmr) => AnnotationClassMetamodelReferenceVariadic(mmr, mmr);
@nomodel
annotationClassMetamodelReference(`class Anything`)
annotationClassMetamodelReference(`value process`)
//illegal annotationClassMetamodelReference(`List<String>`)
annotationClassMetamodelReference(`interface Declaration`)
annotationClassMetamodelReference(`interface List`)
annotationClassMetamodelReference(`value List.size`)
annotationClassMetamodelReference(`function List.get`)
annotationClassMetamodelReference(`function sort`)
annotationClassMetamodelReference(`value true`)
//illegal annotationClassMetamodelReference(`sort<String>`)
// TODO annotationClassMetamodelReference(`package`)
// TODO annotationClassMetamodelReference(`package ceylon.language`)
// TODO annotationClassMetamodelReference(`module`)
// TODO annotationClassMetamodelReference(`module ceylon.language`)
annotationClassMetamodelReferenceLiteral
annotationClassMetamodelReferenceDefaulted1(`class Anything`)
annotationClassMetamodelReferenceDefaulted2()
annotationClassMetamodelReferenceVariadic1()
annotationClassMetamodelReferenceVariadic1(`class Anything`)
annotationClassMetamodelReferenceVariadic1(`class Anything`, `value process`)
// TODO literals for inner classes and interfaces
annotationClassMetamodelReferenceVariadic2()
annotationClassMetamodelReferenceVariadic3(`value false`)
class AnnotationClassMetamodelReference_callsite() {}
