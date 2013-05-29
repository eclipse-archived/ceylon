import ceylon.language.metamodel {
    AppliedInterface = Interface,
    AppliedType
}

shared interface InterfaceDeclaration
        satisfies ClassOrInterfaceDeclaration {
    
    shared formal actual AppliedInterface<Anything> apply(AppliedType* types);
    
    shared formal actual AppliedInterface<Anything> bindAndApply(Object instance, AppliedType* types);
}
