import ceylon.language.meta.model {
    Class,
    MemberClass,
    AppliedType = Type
}

shared interface ClassDeclaration
        satisfies ClassOrInterfaceDeclaration & FunctionalDeclaration {
    
    shared formal Boolean abstract;

    shared formal Boolean anonymous;

    shared formal Boolean final;

    shared formal Class<Type, Arguments> classApply<Type=Anything, Arguments=Nothing>(AppliedType<Anything>* typeArguments)
        given Arguments satisfies Anything[];

    shared formal MemberClass<Container, Type, Arguments> memberClassApply<Container=Nothing, Type=Anything, Arguments=Nothing>(AppliedType<Container> containerType, AppliedType<Anything>* typeArguments)
        given Arguments satisfies Anything[];
}