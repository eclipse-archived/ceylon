import ceylon.language.metamodel{Annotated}
import ceylon.language.metamodel{MetamodelAnnotation = Annotation}

shared interface AnnotatedDeclaration of TopLevelOrMemberDeclaration
                                       | Module
                                       | Package
    satisfies Declaration & Annotated {

    "The annotation instances of the given 
     annotation type on this declaration."
    shared formal Annotation[] annotations<out Annotation>()
        given Annotation satisfies MetamodelAnnotation<Annotation>;
}
