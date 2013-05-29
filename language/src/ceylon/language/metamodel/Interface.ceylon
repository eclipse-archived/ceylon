import ceylon.language.metamodel.declaration {
    InterfaceDeclaration
}

shared interface Interface<out Type>
    satisfies ClassOrInterface<Type> {
    
    shared formal actual InterfaceDeclaration declaration;
}
