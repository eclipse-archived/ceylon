import ceylon.language.model{Annotated}

shared interface TopLevelOrMemberDeclaration of FunctionOrValueDeclaration |
                                                ClassOrInterfaceDeclaration |
                                                AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {

    shared formal Boolean actual;

    shared formal Boolean formal;

    shared formal Boolean default;

    shared formal Boolean shared;

    // FIXME: that name sucks
    shared formal Package packageContainer;
    
    shared formal Boolean toplevel;
}