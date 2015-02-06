import ceylon.language.meta.model {
    Attribute,
    Type
}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter
}

"Contract for flattening the state of an instance of a class when the 
 instance is being serialized.
  
 This interface is implemented by a serialization library."
shared interface Deconstructor {
    
    "Add the given `outer` instance (for an instance of a member class) 
     to the flattened state."
    shared formal void putOuterInstance<Instance>(Instance outerInstance);
    
    "Add the value of the given attribute to the flattened state."
    throws (`class AssertionError`,
        "if there is already a value for the given attribute")
    shared formal void putValue<Instance>(ValueDeclaration attribute, Instance referenced);
    
    "Add an array element to the flattened state."
    shared formal void putElement<Instance>(Integer index, Instance referenced);
}

