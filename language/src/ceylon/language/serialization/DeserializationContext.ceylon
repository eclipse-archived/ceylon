/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.model {
    ClassModel
}
import ceylon.language.meta.declaration {
    ValueDeclaration
}

"""A contract for identifying instances, specifying their classes, attributes, 
   elements and values, and ultimately reconstructing those instances.
 
   Instances are identified using the [[Id]]'s semantics for equality. The 
   methods of this interface can be called in any order; the id serves to 
   associate each method invocation with the instance(s) to pertains to. 
   The only constraint is that [[reconstruct]] will throw if the context 
   lacks enough information to fully initialize the requested instance 
   _or any instance reachable from it_. Reference cycles are supported.
 
   For example, given
 
       serializable class Person(name, employer) {
           shared String name;
           shared Company employer;
       }
       serializable class Company(name) {
           shared String name;
           shared late Person owner;
       }
     
   And an instance graph corresponding to:
 
       value wonkaInc = Company("Wonka Inc.");
       value willy = Person("Willy Wonka", wonkaInc);
       value umpaLumpa = Person("Umpa lumpa", wonkaInc);
       wonkaInc.owner = willy;

   Then we could reconstruct that instance graph like so:
   
       value dc = deserialization<String>();
       
       dc.attribute("ww", `value Person.name`, "wwn");
       dc.attribute("ww", `value Person.employer`, "wi");
       dc.attribute("ul", `value Person.name`, "uln");
       dc.attribute("ul", `value Person.employer`, "wi");
       dc.attribute("wi", `value Company.name`, "win");
       dc.attribute("wi", `value Company.owner`, "ww");
       
       dc.instanceValue("win", "Wonka Inc.");
       dc.instanceValue("wwn", "Willy Wonka");
       dc.instanceValue("uln", "Umpa lumpa");
       
       dc.instance("wi", `Company`);
       dc.instance("ww", `Person`);
       dc.instance("ul", `Person`);
       
       value wonkaInc2 = dc.reconstruct<Company>("wi");
       value willy2 = dc.reconstruct<Person>("ww");
       value umpaLumpa2 = dc.reconstruct<Person>("ul");
       
       assert(wonkaInc2.owner === willy2);
       assert(willy2.employer === wonkaInc2);
       assert(umpaLumpa2.employer === wonkaInc2);
       
   The calls to [[attribute]], [[instanceValue]] and [[instance]] could be 
   in any order.
"""
shared sealed interface DeserializationContext<Id> {
    
    """The given [[instanceId]] refers to an instance of the given class."""
    throws(class DeserializationException, 
        "the given instance was specified by [[instanceValue]] or has already been reconstructed.")
    shared formal void instance(Id instanceId, ClassModel<> clazz);
    
    """The given [[instanceId]] is a member of the instance with the given [[containerId]].
       
       This is used for member class instances."""
    throws(class DeserializationException, 
        "the given instance was specified by [[instanceValue]] or has already been reconstructed.")
    shared formal void memberInstance(Id containerId, Id instanceId);
    
    """The value of the given [[attribute]] of the instance with 
       the given [[instanceId]] has given [[attributeValueId]]."""
    throws(class DeserializationException, 
        "the given instance was specified by [[instanceValue]] or has already been reconstructed.")
    shared formal void attribute(Id instanceId, ValueDeclaration attribute, Id attributeValueId);
    
    """The value at the given [[index]] of the [[Array]] instance with 
       the given [[instanceId]] has given [[elementValueId]]."""
    throws(class DeserializationException, 
        "the given instance was specified by [[instanceValue]] or has already been reconstructed.")
    shared formal void element(Id instanceId, Integer index, Id elementValueId);
    
    """The instance with the given [[instanceId]] has the given value.
       
       This can used to register non-serializable instances with the context, 
       for example object declarations."""
    shared formal void instanceValue(Id instanceId, Anything instanceValue);
    
    """Get the instance with the given [[instanceId]] reconstructing it 
       if necessary."""
    throws(class DeserializationException, 
        "the instance, or an instance reachable from it, 
         could not be reconstructed")
    shared formal Instance reconstruct<Instance>(Id instanceId);
    
    /*shared formal void factory(Id instanceId, Type type);
    shared formal void factory2(Id instanceId, Callable f);
    shared formal void argument(Id instanceId, Id argumentId);
    shared formal void argumentValue(Id instanceId, Anything argumentValue);*/
}


