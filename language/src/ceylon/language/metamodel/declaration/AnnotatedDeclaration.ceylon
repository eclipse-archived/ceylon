import ceylon.language.metamodel{Annotated}

shared interface AnnotatedDeclaration of TopLevelOrMemberDeclaration
                                       | ParameterDeclaration
                                       | Module
                                       | Package
    satisfies Declaration {
}
