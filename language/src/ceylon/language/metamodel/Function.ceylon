import ceylon.language.metamodel.declaration {
    FunctionDeclaration
}

shared interface Function<out Type, in Arguments>
        satisfies Callable<Type, Arguments> & Declaration 
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal AppliedType type;
}
