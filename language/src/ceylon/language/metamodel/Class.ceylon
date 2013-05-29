import ceylon.language.metamodel.declaration {
    ClassDeclaration
}

shared interface Class<out Type, in Arguments>
    satisfies ClassOrInterface<Type> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
    
    shared formal actual ClassDeclaration declaration;
}
