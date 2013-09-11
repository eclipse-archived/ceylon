import ceylon.language.model.declaration {
    FunctionDeclaration,
    TypeParameter
}
import ceylon.language.model {
    ClosedType = Type
}

shared interface FunctionModel<out Type=Anything, in Arguments=Nothing>
        satisfies Model & Generic
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal ClosedType<Type> type;
}
