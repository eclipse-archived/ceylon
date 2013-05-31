import ceylon.language.metamodel{Annotated}

shared interface AnnotatedDeclaration of TopLevelOrMemberDeclaration
                                       | Parameter
                                       | Module
                                       | Package
    satisfies Declaration {
}
