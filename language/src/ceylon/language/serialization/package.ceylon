"The Ceylon serialization infrastructure.
 
 This package provides an API used by *serialization frameworks* to 
 provide serialization services to client code. It does not 
 provide a serialization service itself.
 
 ## Responsibilities
 
 A serialization framework will typcially implement a some kind of 
 serializer and deserializer pair using the facilities of this API, 
 with the following responsibilities:

 This API:
 
 * will cope with shared references and cyclic references in object graphs
 * will prevent access to uninitialized instances, 
   (including instances reachable from other instances, transitively) 
   from both the client and the serialization framework,
 * cannot prevent serialization frameworks from obtaining access to 
   non-`shared` state of instances (loss of encapsulation is 
   an inevitable part of serialization).
   
 The serializer:
 
 * the serializer will obtain a [[SerializationContext]] by 
   calling [[serialization()]] ,
 * the framework will implement [[Deconstructor]] in order to 
   be notified about instances being serialized,
 * the framework will be responsible for adding instances to the 
   [[SerializationContext]], including instances reachable from those 
   specifically added by the client, 
   according to framework-specific rules.
 
 The deserializer:
 
 * the deserializer will obtain a [[DeserializationContext]] by 
   calling [[deserialization()]]
 * the framework sill implement [[Deconstructed]] in order to 
   provide serialized state to instances being reconstructed.
 * the framework will be responsible for registering all the 
   deconstructed instances required for complete deserialization
   of an instance (including its reachable references) 
   before attempting to obtain the instance  
 
"
by ("Gavin", "Tom")
shared package ceylon.language.serialization;
