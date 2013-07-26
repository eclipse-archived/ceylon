import ceylon.language.metamodel{Annotated}

shared interface TopLevelOrMemberDeclaration of FunctionOrValueDeclaration |
                                                ClassOrInterfaceDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {
    
    // FIXME: that name sucks
    shared formal Package packageContainer;
    
    shared formal Boolean toplevel;
}