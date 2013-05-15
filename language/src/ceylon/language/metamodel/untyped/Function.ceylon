import ceylon.language.metamodel { 
    AppliedFunction = Function,
    AppliedType,
    AppliedMember = Member,
    AppliedClassOrInterface = ClassOrInterface
}

shared interface Function 
        satisfies Declaration & Parameterised {

    shared formal Type type;

    shared formal Parameter[] parameters;

    shared formal Sequence<Parameter[]> parameterLists;

    shared formal AppliedFunction<Anything, Nothing> apply(AppliedType* types);

    shared formal AppliedFunction<Anything, Nothing> bindAndApply(Object instance, AppliedType* types);

    shared formal AppliedMember<Container, Kind> memberApply<Container, Kind>(AppliedType* types)
        given Kind satisfies AppliedFunction<Anything, Nothing>;
}