import ceylon.language.meta.model {
    Attribute,
    Type
}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter
}

"Contract for flattening the state of an instance of a class. 
 This interface is implemented by a serialization library."
shared interface Deconstructor {
    
    "Adds the value of the given attribute to the flattened state."
    throws (`class AssertionError`,
        "if there is already a value for the given attribute")
    shared formal void putValue<Type>(ValueDeclaration attribute, Type referenced);
    
    "Adds the given type argument of the given type parameter to the flattened state."
    throws (`class AssertionError`,
        "if there is already a value for the given type argument")
    shared formal void putTypeArgument(TypeParameter typeParameter, Type typeArgument);
}

"The flattened state of an instance of a class.
 This interface is implemented by a serialization library."
shared interface Deconstructed
        satisfies {[ValueDeclaration, Anything]*} {
    
    "Get the value of the given attribute."
    throws (`class AssertionError`,
        "if the value is missing")
    shared formal Type|Reference<Type> getValue<Type>(ValueDeclaration attribute);
    
    "Get the type argument of the given type parameter"
    throws (`class AssertionError`,
        "if the type argument is missing")
    shared formal Type getTypeArgument(TypeParameter typeParameter);
}
