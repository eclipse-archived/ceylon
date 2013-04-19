import ceylon.language.metamodel{SequencedAnnotation, OptionalAnnotation, Type}
@nomodel
annotation class AnnotationClassMetamodelReference(Anything mmr) satisfies SequencedAnnotation<AnnotationClassMetamodelReference, Type<Anything>>{}
@nomodel
annotation class AnnotationClassMetamodelReferenceDefaulted(Anything mmr=AnnotationClassMetamodelReference) satisfies SequencedAnnotation<AnnotationClassMetamodelReferenceDefaulted, Type<Anything>>{}
@nomodel
annotation class AnnotationClassMetamodelReferenceVariadic(Anything* mmrs) satisfies SequencedAnnotation<AnnotationClassMetamodelReferenceVariadic, Type<Anything>>{}
@nomodel
annotation AnnotationClassMetamodelReference annotationClassMetamodelReference(Anything mmr) => AnnotationClassMetamodelReference(mmr);
@nomodel
annotationClassMetamodelReference(Anything)
annotationClassMetamodelReference(process)
class AnnotationClassMetamodelReference_callsite() {}