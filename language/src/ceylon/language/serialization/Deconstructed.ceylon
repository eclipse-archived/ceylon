import ceylon.language.meta.model {
    Attribute
}
import ceylon.language.meta.declaration {
    ValueDeclaration
}

"Contract for flattening the state of an instance of a class. 
 This interface is implemented by a serialization library."
shared interface Deconstructor {
    "Adds the value the given attribute to the flattened state"
    throws (`class AssertionError`,
        "if there is already a vale for the given attribute")
    shared formal void put<Type>(ValueDeclaration attribute, Type referenced);
}

"The flattened state of an instance of a class.
 This interface is implemented by a serialization library."
shared interface Deconstructed
        satisfies {[ValueDeclaration, Anything]*} {
    "Get the value of the given attribute."
    throws (`class AssertionError`,
        "if the value is missing")
    shared formal Type|Reference<Type> get<Type>(ValueDeclaration dec);
}
