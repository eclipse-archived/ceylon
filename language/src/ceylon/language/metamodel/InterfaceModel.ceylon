import ceylon.language.metamodel.declaration {
    InterfaceDeclaration
}

shared interface InterfaceModel<out Type>
    satisfies ClassOrInterface<Type> {
    
    shared formal actual InterfaceDeclaration declaration;
}
