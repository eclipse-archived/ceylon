import ceylon.language.metamodel{Annotated}

shared interface ParameterDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {
    
    shared formal Boolean defaulted;
    
    shared formal Boolean variadic;
}