import ceylon.language.meta.model {
    Class,
    MemberClass
}

"A context representing deserialization of many objects from
 a given input stream. 
 
 The serialization library obtains an instance by calling 
 [[deserialization]] and is then 
 responsible for processing the stream and registering the
 [[deconstructed instances|reference]] with the context.
 The 
 [[Deconstructed state|Deconstructed]] of each instance 
 can be [[supplied|ceylon.language.serialization::DeserializableReference.deserialize]]
 to its reference and finally a complete 
 [[instance|ceylon.language.serialization::RealizableReference.instance]], can be obtained 
 and returned to the client.
 "
shared sealed
interface DeserializationContext
        satisfies {Reference<Anything>*} {
    
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
    
    "Obtain the reference previously registered with the given id, or null
     if the given id has not been registered."
    shared formal Reference<Instance>? getReference<Instance>(Object id);
}
