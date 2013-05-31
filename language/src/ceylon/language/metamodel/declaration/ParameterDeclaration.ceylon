import ceylon.language.metamodel{Annotated}

shared interface ParameterDeclaration
        satisfies Annotated & AnnotatedDeclaration {
    
    shared formal OpenType type;
}