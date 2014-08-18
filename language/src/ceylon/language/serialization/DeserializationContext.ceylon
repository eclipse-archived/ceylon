import ceylon.language.meta.model {
    ClassModel
}

"A context representing deserialization of many objects from
 a given input stream. The serialization library is 
 responsible for processing the stream and registering the
 [[deconstructed states|Deconstructed]] of the objects with
 the context. Then, it may obtain a reference to a fully
 deconstructed object via [[StatefulReference.instance]],
 and return it to the client.
 
 The serialization library obtains an instance by calling 
 [[deserialization()]]."
shared sealed
interface DeserializationContext
        satisfies {Reference<Object?>*} {
    
    "Obtain a reference to the instance of [[Class]] with 
     the given [[identifer|id]]."
    shared formal Reference<Instance> reference<Instance>(Object id, ClassModel<Instance> clazz);
}
