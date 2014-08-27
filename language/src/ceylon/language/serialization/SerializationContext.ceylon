"A context representing serialization of many objects to a 
 single output stream. The client is responsible for 
 [[registering|reference]] the objects to be serialized with the context, 
 assigning them each a unique identifier. Then, the 
 serialization library is responsible for iterating the 
 registered objects in the context and persisting their 
 [[deconstructed states|Deconstructed]] to the output 
 stream.
 
 The serialization library obtains an instance by calling 
 [[serialization()]]."
shared sealed
interface SerializationContext
        satisfies {SerializableReference<Object?>*} /*& Category<Object>*/ {
    // XXX Category<Object> but ought to be Category<Object?>, surely?
    "Create a reference to the given [[instance]] of 
     [[Class]], assigning it the given [[identifer|id]]."
    throws (`class AssertionError`,
        "if there is already an instance with the given
         identifier")
    shared formal
    SerializableReference<Instance> reference<Instance>(Object id, Instance instance);
    
    "An iterator over each of the objects which have 
     been [[registered|reference]] with this context."
    shared actual formal
    Iterator<SerializableReference<Object?>> iterator();
    
    /*"Whether the given instance has been registered with this context"
    shared actual formal
    Boolean contains(Object instance);*/
    
    // TODO What happens if reference is called during an iteration?
}
