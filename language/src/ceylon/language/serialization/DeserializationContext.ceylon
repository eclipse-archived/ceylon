import ceylon.language.meta.model {
    Class,
    MemberClass
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
    
    "Obtain a reference to the instance of toplevel [[Class]] with 
     the given [[identifer|id]]."
    shared formal Reference<Instance> reference<Instance>(
        "The id of the instance."
        Object id,
        "The class of the instance."
        Class<Instance> clazz);
    
    "Obtain a reference to the instance of [[MemberClass]] with 
     the given [[identifer|id]] and outer instance reference."
    shared formal Reference<Instance> memberReference<Outer,Instance>(
        "The id of the instance."
        Object id,
        "The class of the instance."
        MemberClass<Outer,Instance> clazz,
        "A reference to the outer instance if the the 
         [[clazz]]."
        Reference<Outer>? outerReference);
}
