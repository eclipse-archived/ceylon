import ceylon.language.metamodel.declaration {
    FunctionDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

shared interface Function<out Type, in Arguments>
        satisfies Callable<Type, Arguments> & Declaration 
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal ClosedType type;
}
