import ceylon.language.model{SequencedAnnotation, OptionalAnnotation, Annotation}
import ceylon.language.model.declaration { Declaration, ClassOrInterfaceDeclaration }

@nomodel
annotation class AnnotationClassMetamodelReference(Declaration mmr) satisfies SequencedAnnotation<AnnotationClassMetamodelReference, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassMetamodelReferenceDefaulted(Declaration mmr=`AnnotationClassMetamodelReference`) satisfies SequencedAnnotation<AnnotationClassMetamodelReferenceDefaulted, ClassOrInterfaceDeclaration>{}
@nomodel
annotation class AnnotationClassMetamodelReferenceVariadic(Declaration* mmrs) satisfies SequencedAnnotation<AnnotationClassMetamodelReferenceVariadic, ClassOrInterfaceDeclaration>{}
@nomodel
annotation AnnotationClassMetamodelReference annotationClassMetamodelReference(Declaration mmr) => AnnotationClassMetamodelReference(mmr);
@nomodel
annotation AnnotationClassMetamodelReferenceDefaulted annotationClassMetamodelReferenceDefaulted1(Declaration mmr) => AnnotationClassMetamodelReferenceDefaulted(mmr);
@nomodel
annotation AnnotationClassMetamodelReferenceDefaulted annotationClassMetamodelReferenceDefaulted2() => AnnotationClassMetamodelReferenceDefaulted();
@nomodel
annotation AnnotationClassMetamodelReferenceVariadic annotationClassMetamodelReferenceVariadic1(Declaration* mmr) => AnnotationClassMetamodelReferenceVariadic(*mmr);
@nomodel
annotation AnnotationClassMetamodelReferenceVariadic annotationClassMetamodelReferenceVariadic2() => AnnotationClassMetamodelReferenceVariadic();
// FIXME: not supported yet
//@nomodel
//annotation AnnotationClassMetamodelReferenceVariadic annotationClassMetamodelReferenceVariadic3(Declaration mmr) => AnnotationClassMetamodelReferenceVariadic(mmr, mmr);
@nomodel
annotationClassMetamodelReference(`Anything`)
annotationClassMetamodelReference(`process`)
annotationClassMetamodelReferenceDefaulted1(`Anything`)
annotationClassMetamodelReferenceDefaulted2()
annotationClassMetamodelReferenceVariadic1()
annotationClassMetamodelReferenceVariadic1(`Anything`)
annotationClassMetamodelReferenceVariadic1(`Anything`, `process`)
// FIXME: not supported yet
//annotationClassMetamodelReferenceVariadic2()
class AnnotationClassMetamodelReference_callsite() {}
