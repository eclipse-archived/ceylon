import ceylon.language.model { 
    AppliedFunction = Function,
    Type,
    Method,
    AppliedMember = Member,
    AppliedClassOrInterface = ClassOrInterface
}

shared interface FunctionDeclaration
        satisfies FunctionOrValueDeclaration & GenericDeclaration & FunctionalDeclaration {

    shared formal AppliedFunction<Anything, Nothing> apply(Type<Anything>* types);

    shared formal AppliedFunction<Anything, Nothing> bindAndApply(Object instance, Type<Anything>* types);

    shared formal Method<Container, MethodType, Arguments> memberApply<Container, MethodType, Arguments>(Type<Anything>* types)
        given Arguments satisfies Anything[];
}