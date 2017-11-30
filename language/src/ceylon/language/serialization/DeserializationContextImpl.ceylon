/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta {
    type, 
    typeLiteral
}
import ceylon.language.meta.model {
    ClassModel
}
import ceylon.language.serialization {
    DeserializationException
}
import ceylon.language.impl {
    MemberImpl, ElementImpl
}

"Implementation of [[DeserializationContext]] using a few native helper classes."
class DeserializationContextImpl<Id>() satisfies DeserializationContext<Id> 
        given Id satisfies Object {
    """The `Item` in the instances map is either a `Partial` or the actual instance
       that's not ambiguous because `Partial` never leaks, so it's impossible
       for a client to use the API to instantiate a `Partial`
       they can only end up in the map due to our implementation.
    """
    NativeMap<Id,Anything> instances = NativeMap<Id,Anything>();
    
    """a cache of "attribute" (represented as a TypeDescriptor and an attribute name)
       to its type"""
    shared NativeMap<Object->String, Object> memberTypeCache = NativeMap<Object->String, Object>();
    
    """Get the [[Partial]] or instance with the given id"""
    shared Anything leakInstance(Id id) => instances.get(id);
    
    shared actual void attribute(Id instanceId, ValueDeclaration attribute, Id attributeValueId) {
        attributeOrElement(instanceId, MemberImpl(attribute), attributeValueId);
    }
    
    "A [[DeserializationException]] to day that the instance with the given id has already been instantiated."
    shared DeserializationException alreadyComplete(Id instanceId) 
        => DeserializationException("instance referred to by id ``instanceId`` already complete.");
    
    "Get or create the [[Partial]] for `instanceId`; 
     add the given `attributeValueId` to its partial state."
    throws(class DeserializationException, 
        "If the `instanceId` corresponds to a reconstructed 
         instance or to a partial that's already been instantiated")
    void attributeOrElement(Id instanceId, ReachableReference attributeOrIndex, Id attributeValueId) {
        Partial referring;
        switch(r=instances.get(instanceId))
        case (is Null){
            value p = PartialImpl(instanceId);
            instances.put(instanceId, p);
            referring = p;
        }
        case (is Partial) {
            referring = r;
            if (referring.instantiated) {
                throw alreadyComplete(instanceId);
            }
        }
        else {//referring is an instance
            throw alreadyComplete(instanceId);
        }
        referring.addState(attributeOrIndex, attributeValueId);
    }
    
    shared actual void element(Id instanceId, Integer index, Id elementValueId) {
        attributeOrElement(instanceId, ElementImpl(index), elementValueId);
    }
    
    shared actual void instance(Id instanceId, ClassModel<Anything,Nothing> clazz) {
        if (!clazz.declaration.serializable) {
            throw DeserializationException("not serializable: ``clazz``");
        }
        getOrCreatePartial(instanceId).clazz = clazz;
    }
    
    "Get or create a [[Partial]] for the given `instanceId`."
    Partial getOrCreatePartial(Id instanceId) {
        Partial partial;
        switch(r=instances.get(instanceId))
        case (is Null) {
            value p = PartialImpl(instanceId);
            instances.put(instanceId, p);
            partial = p;
        }
        case (is Partial) {
            partial = r;
        }
        else {// an instance
            throw alreadyComplete(instanceId);
        }
        return partial;
    }
    
    shared actual void memberInstance(Id containerId, Id instanceId) {
        Anything container;
        switch(r=instances.get(containerId))
        case (is Null) {
            value p = PartialImpl(containerId);
            instances.put(containerId, p);
            container = p;
        }
        else {
            container = r;
        }
        getOrCreatePartial(instanceId).container = container;
    }
    
    shared actual void instanceValue(Id instanceId, Anything instanceValue) {
        instances.put(instanceId, instanceValue);
    }
    
    shared actual Instance reconstruct<Instance>(Id instanceId) {
        NativeDeque deque = NativeDeque();
        value root = instances.get(instanceId);
        if (!root exists) {
            if (instances.contains(instanceId)) {
                assert(is Instance r=null);
                return r;
            } else {
                throw DeserializationException("unknown id: ``instanceId``.");
            }
        }
        deque.pushFront(instances.get(instanceId));
        while (!deque.empty){
            value r=deque.popFront();
            switch(r)
            case (is Null) {
                assert(false);
            }
            case (is Partial) {
                if (r.member, is Partial container = r.container,
                    !container.instantiated) {
                    // On the JVM we need to ensure that all outer instances are 
                    // instantiated before all member instances
                    // so do the container first and then come back for the member
                    deque.pushFront(r);
                    deque.pushFront(container);
                    continue;
                }
                if (!r.instantiated) {
                    r.instantiate();
                }
                // push the referred things on to the stack
                // but only if they haven't yet been instantiated
                for (referredId in r.refersTo) {
                    assert(is Id referredId);
                    value referred = instances.get(referredId);
                    if (is Partial referred,
                        !referred.instantiated) {
                        deque.pushFront(referred);
                    }
                }
            } else {
                // it's an instance already, nothing to do
            }
        }
        // we now have real instances for everything reachable from instanceId
        // so now we can inject the state...
        deque.pushFront(instances.get(instanceId));
        while (!deque.empty){
            switch(r=deque.popFront())
            case (is Partial) {
                if (r.member) {
                    if (is Partial container = r.container,
                        !container.initialized) {
                        deque.pushFront(r);
                        deque.pushFront(container);
                        continue;
                    }
                }
                if (!r.initialized) {
                    // push the referred things on to the stack
                    // but only if they haven't yet been initialized
                    for (referredId in r.refersTo) {
                        assert(is Id referredId);
                        value referred = instances.get(referredId);
                        if (is Partial referred,
                            !referred.initialized) {
                            deque.pushFront(referred);
                        }
                    }
                    if (r.member,
                        is Partial container = r.container,
                            !container.initialized) {
                        deque.pushFront(container);
                    }
                    r.initialize<Id>(this);
                }
            } else {
                // it's an instance already, nothing to do
            }
        }
        
        switch(r=instances.get(instanceId))
        case (is Partial) {
            value result=r.instance();
            if (is Instance result) {
                instances.put(instanceId, result);
                return result;
            } else {
                throw DeserializationException("instance with id ``instanceId`` has class ``type(result)`` not assignable to given type ``typeLiteral<Instance>()``");
            }
        }
        else {
            if (is Instance r) {
                return r;
            } else {
                throw DeserializationException("instance with id ``instanceId`` has class ``type(r)`` not assignable to given type ``typeLiteral<Instance>()``");
            }
        }
        
    }
    
}

