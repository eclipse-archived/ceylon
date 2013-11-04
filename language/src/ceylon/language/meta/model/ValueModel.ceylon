import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta.model {
    ClosedType = Type
}

"""A value model represents the model of a Ceylon value that you can inspect.
   
   A value model can be either a toplevel [[Value]] or a member [[Attribute]].
 """
shared interface ValueModel<out Get=Anything, in Set=Nothing>
        satisfies Model {

    "This value's declaration."
    shared formal actual ValueDeclaration declaration;
    
    "This value's closed type."
    shared formal ClosedType<Get> type;
}
