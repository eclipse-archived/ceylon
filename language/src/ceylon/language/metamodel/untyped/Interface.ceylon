import ceylon.language.metamodel {
    AppliedInterface = Interface,
    AppliedType
}

shared interface Interface
        satisfies ClassOrInterface {
    
    shared formal actual AppliedInterface<Anything> apply(AppliedType* types);
}
