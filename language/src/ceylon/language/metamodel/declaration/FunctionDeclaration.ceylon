import ceylon.language.metamodel { 
    AppliedFunction = Function,
    Type,
    AppliedMember = Member,
    AppliedClassOrInterface = ClassOrInterface
}

shared interface FunctionDeclaration
        satisfies TopLevelOrMemberDeclaration & GenericDeclaration & FunctionalDeclaration {

    shared formal OpenType type;

    shared formal AppliedFunction<Anything, Nothing> apply(Type* types);

    shared formal AppliedFunction<Anything, Nothing> bindAndApply(Object instance, Type* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(Type* types)
        given Kind satisfies AppliedFunction<Anything, Nothing>;
}