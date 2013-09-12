import ceylon.language.model {
    AppliedClass = Class,
    Type
}

shared interface ClassDeclaration
        satisfies ClassOrInterfaceDeclaration & FunctionalDeclaration {
    
    shared formal Boolean abstract;

    shared formal Boolean anonymous;

    // FIXME: add final
    
    // FIXME: parameterise
    shared formal actual AppliedClass<Anything, Nothing> apply(Type<Anything>* types);

    // FIXME: add
    //shared formal actual MemberClass<Anything, Anything, Nothing> applyMember(Type<Anything>* types);
    
    // FIXME: remove
    shared formal actual AppliedClass<Anything, Nothing> bindAndApply(Object instance, Type<Anything>* types);
}