import ceylon.language{AnnotationType = Annotation}

shared interface AnnotatedDeclaration of NestableDeclaration
                                       | Module
                                       | Package
    satisfies Declaration & Annotated {

    "The annotation instances of the given 
     annotation type on this declaration."
    shared formal Annotation[] annotations<out Annotation>()
        given Annotation satisfies AnnotationType<Annotation>;
}
