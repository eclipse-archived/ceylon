import ceylon.language.model{Annotated}

// FIXME: new name NestableDeclaration?
shared interface TopLevelOrMemberDeclaration of FunctionOrValueDeclaration |
                                                ClassOrInterfaceDeclaration |
                                                AliasDeclaration
        satisfies AnnotatedDeclaration & TypedDeclaration {

    shared formal Boolean actual;

    shared formal Boolean formal;

    shared formal Boolean default;

    shared formal Boolean shared;

    // FIXME: that name sucks: containingPackage?
    shared formal Package packageContainer;
    // FIXME: add containingModule?

    // FIXME: introduce interface ContainedDeclaration { shared formal AnnotatedDeclaration container; }?
    // -> no interface but union
    // that would allow to go all the way up to Module as long as the container is a ContainedDeclaration.
    // Alternatively we could make this an attribute of AnnotatedDeclaration and make it optional, where for Module it would be null,
    // which would allow us to go all the way up to the module.
    // Other question is should TypeParameter have a container? I don't think we consider it a member, so not sure
    shared formal AnnotatedDeclaration container;
    
    shared formal Boolean toplevel;
}