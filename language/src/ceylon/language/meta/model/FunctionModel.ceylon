import ceylon.language.meta.declaration {
    FunctionDeclaration,
    TypeParameter
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"""A function model represents the model of a Ceylon function that you can inspect.
   
   A function model can be either a toplevel [[Function]] or a member [[Method]].
 """
shared interface FunctionModel<out Type=Anything, in Arguments=Nothing>
        satisfies Model & Generic
        given Arguments satisfies Anything[] {

    "This function's declaration."
    shared formal actual FunctionDeclaration declaration;

    "This function's return closed type."
    shared formal ClosedType<Type> type;
    
    "This function's parameter closed types"
    shared formal ClosedType<Anything>[] parameterTypes;
}
