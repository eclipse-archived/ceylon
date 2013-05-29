import ceylon.language.metamodel.declaration {
    UntypedInterface = Interface
}

shared interface Interface<out Type>
    satisfies ClassOrInterface<Type> {
    
    shared formal actual UntypedInterface declaration;
}
