import ceylon.language.meta.model {
    Attribute,
    Type
}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter
}

"The flattened state of an instance of a class.
 This interface is implemented by a serialization library."
shared interface Deconstructed
        satisfies {[ValueDeclaration, Anything]*} {
    
    "Get the outer instance."
    shared formal Reference<Instance>? getOuterInstance<Instance>();
    
    "Get the type argument of the given type parameter"
    throws (`class AssertionError`,
        "if the type argument is missing")
    shared formal Type getTypeArgument(TypeParameter typeParameter);
    
    "Get the value of the given attribute."
    throws (`class AssertionError`,
        "if the value is missing")
    shared formal Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute);
    
    shared formal Instance|Reference<Instance> getElement<Instance>(Integer index);
    
    //shared formal Array<Element>
    //getArray<Element>(ValueDeclaration attribute);
}

