import ceylon.language.meta.declaration { Declaration, ClassOrInterfaceDeclaration }

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

shared abstract class ClassAlias() => Object();
shared interface InterfaceAlias => Declaration;
shared alias TypeAlias => Declaration;

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
annotationClassMetamodelReference(`class ClassAlias`)
annotationClassMetamodelReference(`interface InterfaceAlias`)
annotationClassMetamodelReference(`alias TypeAlias`)
//illegal annotationClassMetamodelReference(`sort<String>`)
// annotationClassMetamodelReference(`package`)
annotationClassMetamodelReference(`package ceylon.language.meta`)
annotationClassMetamodelReference(`package ceylon.language`)
annotationClassMetamodelReference(`package org.w3c.dom`)
// annotationClassMetamodelReference(`module`)
annotationClassMetamodelReference(`module ceylon.language`)
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
