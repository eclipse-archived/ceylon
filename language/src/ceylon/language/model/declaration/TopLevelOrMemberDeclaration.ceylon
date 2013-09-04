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

    // FIXME: introduce interface ContainedDeclaration { shared formal AnnotatedDeclaration container; }?
    // that would allow to go all the way up to Module as long as the container is a ContainedDeclaration.
    // Alternatively we could make this an attribute of AnnotatedDeclaration and make it optional, where for Module it would be null,
    // which would allow us to go all the way up to the module.
    // Other question is should TypeParameter have a container? I don't think we consider it a member, so not sure
    shared formal AnnotatedDeclaration container;
    
    shared formal Boolean toplevel;
}