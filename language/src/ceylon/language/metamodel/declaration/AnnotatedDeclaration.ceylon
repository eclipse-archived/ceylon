import ceylon.language.metamodel{Annotated}

shared interface AnnotatedDeclaration of TopLevelOrMemberDeclaration
                                       | ParameterDeclaration
                                       | Module
                                       | Package
    satisfies Declaration & Annotated {

    // FIXME: why is the bound not c.l.m.Annotation?
    shared formal Annotation[] annotations<Annotation>()
        given Annotation satisfies Object;
}
