import ceylon.language.meta.model {
    Interface,
    MemberInterface,
    AppliedType = Type
}

shared interface InterfaceDeclaration
        satisfies ClassOrInterfaceDeclaration {
    
    shared formal Interface<Type> interfaceApply<Type=Anything>(AppliedType<Anything>* typeArguments);

    shared formal MemberInterface<Container, Type> memberInterfaceApply<Container=Nothing, Type=Anything>(AppliedType<Container> containerType, AppliedType<Anything>* typeArguments);
}
