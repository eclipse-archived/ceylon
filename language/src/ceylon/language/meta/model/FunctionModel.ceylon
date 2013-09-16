import ceylon.language.meta.declaration {
    FunctionDeclaration,
    TypeParameter
}
import ceylon.language.meta.model {
    ClosedType = Type
}

shared interface FunctionModel<out Type=Anything, in Arguments=Nothing>
        satisfies Model & Generic
        given Arguments satisfies Anything[] {
    
    shared formal actual FunctionDeclaration declaration;

    shared formal ClosedType<Type> type;
}
