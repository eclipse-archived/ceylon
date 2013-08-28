import ceylon.language.model{Annotated}

shared interface TopLevelOrMemberDeclaration of FunctionOrValueDeclaration |
                                                ClassOrInterfaceDeclaration |
                                                AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {
    
    // FIXME: that name sucks
    shared formal Package packageContainer;
    
    shared formal Boolean toplevel;
}