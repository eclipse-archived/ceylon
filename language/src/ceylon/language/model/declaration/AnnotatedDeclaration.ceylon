import ceylon.language.model{Annotated}
import ceylon.language.model{MetamodelAnnotation = Annotation}

shared interface AnnotatedDeclaration of TopLevelOrMemberDeclaration
                                       | Module
                                       | Package
    satisfies Declaration & Annotated {

    "The annotation instances of the given 
     annotation type on this declaration."
    shared formal Annotation[] annotations<out Annotation>()
        given Annotation satisfies MetamodelAnnotation<Annotation>;
}
