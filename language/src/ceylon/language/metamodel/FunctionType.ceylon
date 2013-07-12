import ceylon.language.metamodel.declaration {
    FunctionDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

// FIXME it's not a type so let's find a better name
shared interface FunctionType<out Type, in Arguments>
        satisfies Model
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal ClosedType type;
}
