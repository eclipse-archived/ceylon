import ceylon.language.metamodel.declaration {
    InterfaceDeclaration
}

shared interface InterfaceType<out Type>
    satisfies ClassOrInterface<Type> {
    
    shared formal actual InterfaceDeclaration declaration;
}

shared interface Interface<out Type>
    satisfies InterfaceType<Type> {}

shared interface MemberInterface<in Container, out Type>
    satisfies InterfaceType<Type> & Member<Container, Interface<Type>> {}
