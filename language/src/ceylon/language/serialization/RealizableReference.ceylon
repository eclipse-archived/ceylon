"A reference with sufficient state to realize an instance."
shared sealed
interface RealizableReference<Instance> satisfies Reference<Instance> {
    
    /*"Get the flattened state of the instance."
    shared formal
    /*Deconstructed<Instance>*/ void serialize(Deconstructor deconstructor);*/
    
    "Get the instance. During deserialization, could force 
     reconstruction."
    throws (`class AssertionError`,
        "if there is a problem reconstructing the object
         or any object it references")
    shared formal
    Instance instance();
    
    "Force reconstruction of the instance."
    throws (`class AssertionError`,
        "if there is a problem reconstructing the object
         or any object it references")
    shared formal
    void reconstruct();
}
