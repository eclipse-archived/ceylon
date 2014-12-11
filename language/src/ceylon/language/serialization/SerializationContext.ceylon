"A context representing serialization of many objects to a 
 single output stream. 
 
 The serialization library obtains an instance by calling 
 [[serialization]] and then  
 [[registers|reference]] the objects to be serialized, 
 assigning them each a unique identifier. Then, the 
 serialization library is responsible for iterating the 
 instances registered with the context and persisting their 
 [[deconstructed states|Deconstructed]] to the output 
 stream.
 "
shared sealed
interface SerializationContext
        satisfies {SerializableReference<Anything>*} {
    "Create a reference to the given [[instance]], assigning it 
     the given [[identifer|id]]."
    throws (`class AssertionError`,
        "if there is already an instance with the given
         identifier")
    shared formal
    SerializableReference<Instance> reference<Instance>(Object id, Instance instance);
    
    "The reference for the given (previously [[registered|reference]]) instance, 
     or null if the given instance has not been [[registered|reference]]."
    shared formal SerializableReference<Instance>? getReference<Instance>(Instance instance);
    
    "An iterator over each of the objects which have 
     been [[registered|reference]] with this context."
    shared actual formal
    Iterator<SerializableReference<Anything>> iterator();
}
