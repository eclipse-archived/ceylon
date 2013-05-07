import ceylon.language.metamodel.untyped {
    UntypedFunction = Function
}

shared interface Function<out Type, in Arguments>
        satisfies Callable<Type, Arguments> & Declaration 
        given Arguments satisfies Anything[] {
    
    shared formal actual UntypedFunction declaration;

    shared formal AppliedType type;
}
