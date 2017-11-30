/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"The Ceylon serialization infrastructure.
 
 This package provides an API for *serialization libraries* to
 provide their own serialization APIs to client code. It does not 
 provide a serialization service itself.
 
 This API:
 
 * provides a way to walk the graph of instances added for serialization, 
   including those reachable via non-\\\`shared\\\` attributes,
 * copes with shared references and cyclic references in object graphs,
 * prevents access to uninitialized and partially initialized instances 
   during deserialization.  
   (this includes instances reachable from other instances, transitively).
   
 The API cannot prevent serialization libraries from obtaining access to 
 non-\\\`shared\\\` state of instances (loss of encapsulation is 
 an inevitable part of serialization).
 
 A serialization library will implement some kind of 
 serializer and/or deserializer (frequently both) using the facilities 
 of this API.
 
 ## Serializers
 
 A *serializer* takes one or many Ceylon instances and serialize them 
 (and the instances reachable from them) to 
 some external representation, usually bytes or text in some specific format. 
 
 A serializer provided by a serialization library:
 
 * obtains a [[SerializationContext]] by 
   calling [[serialization]],
 * uses [[SerializationContext.references]] to find the references that 
   an instance holds,
 * proceeds to traverse the graph of instances according to 
   library-specific rules
 
 ## Deserializers
 
 A *deserializer* constructs a graph of 
 Ceylon instances according to some external representation.
   
 A deserializer provided by a serialization library:
 
 * obtains a [[DeserializationContext]] by 
   calling [[deserialization]]
 * progressively provides information about instances:
 
     * associates an instance with its 
       [[class|DeserializationContext.instance]],
     * associates a member instance with its [[containing instance|DeserializationContext.memberInstance]],
     * associates a referred instance with its [[referring instance and attribute|DeserializationContext.attribute]],
     * associates a referred instance with its [[referring array index|DeserializationContext.element]],
     * associates an instance with a [[given other instance|DeserializationContext.instanceValue]]. 
       This is useful for instances which refer to singletons 
       (on deserialization the singleton value has to be provided, since it 
       cannot be instantiated by the serialization API).
          
 * once the context has complete information about the instance 
   and every instance reachable from it
   the instance can be 
   [[reconstructed|DeserializationContext.reconstruct]].
  
 ## Serializability
 
 *Serializability* is whether or not a particular serialization library can 
 serialize a particular instance. 
 
 Instances of classes which are annotated [[serializable]] are able to 
 enumerate their state during serialization, 
 and initialize their state during deserialization.
 However, that does not imply that every serialization library 
 can necessarily serialize instances of every `serializable` class.
   
 The serializability of an instance can depend on:
 
 * the class of the instance, and its super classes,
 * the serializability of the outer instance(s), if the object is an 
   instance of a member class. ,
 * the serialization library's support for serializing generic classes, 
   and member classes,
 * the serialization library's support for serializing anonymous objects
   (such as `true`, `null` or `smaller`),
 * the underlying serialization format the serialization library supports
    
 The deserializability of an instance can depend on:
 
 * The runtime availability and compatibility of the class of the instances, 
   and its super classes, for each instance in the object graph. 
"
by ("Gavin", "Tom")
tagged("Serialization")
since("1.2.0")
shared package ceylon.language.serialization;
