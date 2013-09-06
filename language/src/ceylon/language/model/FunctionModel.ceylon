import ceylon.language.model.declaration {
    FunctionDeclaration,
    TypeParameter
}
import ceylon.language.model {
    ClosedType = Type
}

shared interface FunctionModel<out Type=Anything, in Arguments=Nothing>
        satisfies Model
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    // FIXME: turn that into an interface
    shared formal Map<TypeParameter, ClosedType<Anything>> typeArguments;

    shared formal ClosedType<Type> type;
}
