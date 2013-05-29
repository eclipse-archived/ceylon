import ceylon.language.metamodel {
    AppliedClass = Class,
    AppliedType
}

shared interface ClassDeclaration
        satisfies ClassOrInterfaceDeclaration {
    shared formal actual AppliedClass<Anything, Nothing> apply(AppliedType* types);

    shared formal actual AppliedClass<Anything, Nothing> bindAndApply(Object instance, AppliedType* types);
    
    shared formal Parameter[] parameters;
}