import ceylon.language.metamodel.declaration {
    UntypedClass = Class
}

shared interface Class<out Type, in Arguments>
    satisfies ClassOrInterface<Type> & Callable<Type, Arguments>
    given Arguments satisfies Anything[] {
    
    shared formal actual UntypedClass declaration;
}
