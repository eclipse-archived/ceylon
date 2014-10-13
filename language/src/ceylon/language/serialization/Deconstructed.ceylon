import ceylon.language.meta.model {
    Attribute,
    Type
}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter
}

"The flattened state of an instance of a class used during deserialization.
 
 This interface is implemented by a serialization library."
shared interface Deconstructed
        satisfies {[ValueDeclaration, Anything]*} {
    
    "The outer instance if the class of the instance is a 
     member class, otherwise null."
    shared formal Reference<Instance>? getOuterInstance<Instance>();
    
    "The type argument of the given type parameter."
    throws (`class AssertionError`,
        "if the type argument is absent")
    shared formal Type getTypeArgument(TypeParameter typeParameter);
    
    "The value of the given attribute."
    throws (`class AssertionError`,
        "if the value is absent")
    shared formal Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute);
    
    shared formal Instance|Reference<Instance> getElement<Instance>(Integer index);
    
    //shared formal Array<Element>
    //getArray<Element>(ValueDeclaration attribute);
}

