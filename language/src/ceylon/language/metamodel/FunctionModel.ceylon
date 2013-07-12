import ceylon.language.metamodel.declaration {
    FunctionDeclaration
}
import ceylon.language.metamodel {
    ClosedType = Type
}

shared interface FunctionModel<out Type, in Arguments>
        satisfies Model
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal ClosedType type;
}
