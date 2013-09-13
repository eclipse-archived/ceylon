import ceylon.language.model{Annotated}

// FIXME: new name NestableDeclaration?
shared interface NestableDeclaration of FunctionOrValueDeclaration |
                                                ClassOrInterfaceDeclaration |
                                                AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {

    shared formal Boolean actual;

    shared formal Boolean formal;

    shared formal Boolean default;

    shared formal Boolean shared;

    shared formal Package containingPackage;

    shared formal Module containingModule;

    shared formal NestableDeclaration|Package container;
    
    shared formal Boolean toplevel;
}