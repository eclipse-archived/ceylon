import ceylon.language.model {
    AppliedInterface = Interface,
    Type
}

shared interface InterfaceDeclaration
        satisfies ClassOrInterfaceDeclaration {
    
    shared formal actual AppliedInterface<Anything> apply(Type<Anything>* types);
    
    shared formal actual AppliedInterface<Anything> bindAndApply(Object instance, Type<Anything>* types);
}
