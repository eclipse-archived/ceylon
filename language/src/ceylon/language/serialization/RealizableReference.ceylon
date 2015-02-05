"A reference with sufficient state to realize an instance."
shared sealed
interface RealizableReference<out Instance> satisfies Reference<Instance> {
    
    /*"Get the flattened state of the instance."
    shared formal
    /*Deconstructed<Instance>*/ void serialize(Deconstructor deconstructor);*/
    
    "The reconstructed instance. This method can force 
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
