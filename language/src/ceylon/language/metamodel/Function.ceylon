import ceylon.language.metamodel.declaration {
    FunctionDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

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

/*
shared interface Method<in Container, out Type, in Arguments>
        satisfies FunctionType<Type, Arguments> & Member<Container, Method<Type, Arguments>>
        given Arguments satisfies Anything[] {
}
*/