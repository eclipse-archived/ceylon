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
        satisfies {[Integer|ValueDeclaration, Anything]*} {
    
    "The outer instance if the class of the instance is a 
     member class, otherwise null."
    shared formal Reference<Instance>? getOuterInstance<Instance>();
    
    "The value of the given attribute."
    throws (`class AssertionError`,
        "if the value is absent")
    shared formal Instance|Reference<Instance> getValue<Instance>(ValueDeclaration attribute);
    
    "The array element at the given index."
    shared formal Instance|Reference<Instance> getElement<Instance>(Integer index);
    
}

