import ceylon.language.metamodel.declaration {
    FunctionDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

// FIXME it's not a type so let's find a better name
shared interface FunctionType<out Type, in Arguments>
        satisfies DeclarationType
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal ClosedType type;
}

shared interface Function<out Type, in Arguments>
        satisfies FunctionType<Type, Arguments> & Callable<Type, Arguments>
        given Arguments satisfies Anything[] {
}

shared interface Method<in Container, out Type, in Arguments>
        satisfies FunctionType<Type, Arguments> & Member<Container, Function<Type, Arguments>>
        given Arguments satisfies Anything[] {
}
