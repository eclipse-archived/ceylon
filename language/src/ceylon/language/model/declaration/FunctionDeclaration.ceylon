import ceylon.language.model { 
    Function,
    Type,
    Method
}

shared interface FunctionDeclaration
        satisfies FunctionOrValueDeclaration & GenericDeclaration & FunctionalDeclaration {

    shared formal Function<Return, Arguments> apply<Return=Anything, Arguments=Nothing>(Type<Anything>* typeArguments)
        given Arguments satisfies Anything[];

    shared formal Method<Container, Return, Arguments> memberApply<Container=Nothing, Return=Anything, Arguments=Nothing>(Type<Container> containerType, Type<Anything>* typeArguments)
        given Arguments satisfies Anything[];
}