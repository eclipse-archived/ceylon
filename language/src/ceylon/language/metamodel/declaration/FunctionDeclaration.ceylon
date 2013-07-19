import ceylon.language.metamodel { 
    AppliedFunction = Function,
    Type,
    Method,
    AppliedMember = Member,
    AppliedClassOrInterface = ClassOrInterface
}

shared interface FunctionDeclaration
        satisfies TopLevelOrMemberDeclaration & GenericDeclaration & FunctionalDeclaration {

    shared formal AppliedFunction<Anything, Nothing> apply(Type* types);

    shared formal AppliedFunction<Anything, Nothing> bindAndApply(Object instance, Type* types);

    shared formal Method<Container, MethodType, Arguments> memberApply<Container, MethodType, Arguments>(Type* types)
        given Arguments satisfies Anything[];
}