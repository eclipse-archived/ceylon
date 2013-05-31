import ceylon.language.metamodel{Annotated}

shared interface TopLevelOrMemberDeclaration of AttributeDeclaration |
                                                FunctionDeclaration |
                                                ClassOrInterfaceDeclaration
        satisfies Annotated & AnnotatedDeclaration {
    
    shared formal Annotation[] annotations<Annotation>()
            given Annotation satisfies Object;
    
    // FIXME: that name sucks
    shared formal Package packageContainer;
    
    shared formal Boolean toplevel;
}