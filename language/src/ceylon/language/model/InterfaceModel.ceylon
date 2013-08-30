import ceylon.language.model.declaration {
    InterfaceDeclaration
}

shared interface InterfaceModel<out Type=Anything>
    satisfies ClassOrInterface<Type> {
    
    shared formal actual InterfaceDeclaration declaration;
}
