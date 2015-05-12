import ceylon.language.meta.declaration {
    FunctionDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"""A function model represents the model of a Ceylon function that you can inspect.
   
   A function model can be either a toplevel [[Function]] or a member [[Method]].
 """
shared sealed interface FunctionModel<out Type, in Arguments>
        satisfies Model & Generic & Functional
        given Arguments satisfies Anything[] {

    "This function's declaration."
    shared formal actual FunctionDeclaration declaration;

    "This function's return closed type."
    shared formal ClosedType<Type> type;
}
