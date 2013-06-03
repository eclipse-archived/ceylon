import ceylon.language.metamodel{Annotated}

shared interface ParameterDeclaration
        satisfies AnnotatedDeclaration {
    
    shared formal OpenType type;
    
    shared formal Boolean defaulted;
}