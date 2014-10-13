import ceylon.language.meta.model {
    ClassModel
}

"A reference with sufficient state to realize an instance."
shared sealed
interface SerializableReference<Instance> satisfies Reference<Instance> {
    
    "Records the state of the instance in a deconstructor obtained 
     from the given function."
    shared formal
    /*Deconstructed<Instance>*/ void serialize(Deconstructor deconstructor(ClassModel classModel));
    
    "The instance."
    Instance instance();
    
    /*"Force reconstruction of the instance."
    throws (`class AssertionError`,
        "if there is a problem reconstructing the object
         or any object it references")
    shared formal
    void reconstruct();*/
}
