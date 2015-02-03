"The Ceylon serialization infrastructure.
 
 This package provides an API for *serialization libraries* to
 provide their own serialization APIs to client code. It does not 
 provide a serialization service itself.
 
 A serialization library will typically implement some kind of 
 serializer and deserializer pair using the facilities of this API. 
 A *serializer* would take one or many Ceylon instances and serialize them 
 (and the instances reachable from them) to 
 some external representation, usually bytes or text in some specific format. 
 A *deserializer* would do the reverse, constructing a graph of 
 Ceylon instances from some external representation. 
  
 ## Responsibilities

 This API:
 
 * provides a way to walk the graph of instances added for serialization, 
   including those reachable via non-`shared` attributes,
 * copes with shared references and cyclic references in object graphs,
 * prevents access to uninitialized and partially initialized instances 
   during deserialization.  
   (this includes instances reachable from other instances, transitively).
   
 The API cannot prevent serialization libraries from obtaining access to 
 non-`shared` state of instances (loss of encapsulation is 
 an inevitable part of serialization).
   
 A serializer provided by a serialization library:
 
 * obtains a [[SerializationContext]] by 
   calling [[serialization]] ,
 * is responsible for [[SerializationContext.reference|adding]] instances to the 
   `SerializationContext`, including instances reachable from those 
   explicitly added by the client, according to library-specific rules
 * then iterates over the [[SerializableReferences]] in 
   the `SerializationContext` generating the external representation 

 Client code will interact with the abstractions 
 provided by the serialization library in order to add instances to 
 be serialized.
   
 A deserializer provided by a serialization library:
 
 * obtains a [[DeserializationContext]] by 
   calling [[deserialization]]
 * [[DeserializationContext.reference|registers]] all the 
   deconstructed instances required for complete deserialization
   of an instance (including its reachable references) by inspecting 
   the information in external representation.
 * then [[DeserializableReference.deserialize|deserializes]] instances 
   by providing a [[Deconstructed]] which yields the 
   serialized state of each instance,
 * finally obtains a reference to the reconstructed instance using 
   [[RealizableReference.instance]].
  
 ## Serializability
 
 *Serializability* is whether or not a particular serialization library can 
 serialize and deserialized a particular instance. 
 
 Instances of classes which are annotated [[serializable]] are able to record 
 their state to a [[Deconstructor]] during serialization, 
 and initialize their state from a [[Deconstructed]] during deserialization.
 However, that does not imply that every serialization library 
 can necessarily serialize instances of every `serializable` class.
   
 The serializability of an instance can depend on:
 
 * the class of the instance, and its super classes
 * the serializability of the outer instance(s), if the object is an 
   instance of a member class.  
 * the serialization library's support for serializing generic classes, 
   and member classes,
 * the serialization library's support for serializing anonymous objects
   (such as `true`, `null` or `smaller`).
   
 The deserializability of an instance can depend on:
 
 * The runtime availability and compatibility of the class of the instances, 
   and its super classes, for each instance in the object graph. 
"
by ("Gavin", "Tom")
shared package ceylon.language.serialization;
