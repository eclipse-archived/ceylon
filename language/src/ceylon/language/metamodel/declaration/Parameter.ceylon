import ceylon.language.metamodel{Annotated}

shared interface Parameter 
        satisfies Annotated & AnnotatedDeclaration {
    
    shared formal OpenType type;
}