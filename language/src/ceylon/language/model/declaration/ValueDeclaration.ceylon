import ceylon.language.model { Value, Attribute, AppliedType = Type }

shared interface ValueDeclaration
        satisfies FunctionOrValueDeclaration {
    
    shared formal Value<Type> apply<Type=Anything>();

    shared formal Attribute<Container, Type> memberApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType);
}