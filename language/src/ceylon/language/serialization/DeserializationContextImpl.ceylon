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
    throws(`class DeserializationException`, 
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
    
    """Traverse the instance graph from the given instance and 
       instantiate each partial.
    """
    void instantiateReachable(NativeDeque stack, Id instanceId) {
        assert(stack.empty);
        stack.pushFront(instances.get(instanceId));
        while (!stack.empty){
            value instance=stack.popFront();
            switch(instance)
            case (is Null) {
                assert(false);
            }
            case (is Partial) {
                if (instance.member, is Partial container = instance.container,
                    !container.instantiated) {
                    // On the JVM we need to ensure that all outer instances are 
                    // instantiated before all member instances
                    // so do the container first and then come back for the member
                    stack.pushFront(instance);
                    stack.pushFront(container);
                    continue;
                }
                if (!instance.instantiated) {
                    instance.instantiate();
                }
                // push the referred things on to the stack
                // but only if they haven't yet been instantiated
                for (referredId in instance.refersTo) {
                    assert(is Id referredId);
                    value referred = instances.get(referredId);
                    if (is Partial referred,
                        !referred.instantiated) {
                        stack.pushFront(referred);
                    }
                }
            } else {
                // it's an instance already, nothing to do
            }
        }
    }
    
    """Traverse the instance graph from the given instance and 
       initialize each partial.
    """
    void initializeReachable(NativeDeque stack, Id instanceId) {
        assert(stack.empty);
        stack.pushFront(instances.get(instanceId));
        while (!stack.empty){
            switch(instance=stack.popFront())
            case (is Partial) {
                if (instance.member) {
                    if (is Partial container = instance.container,
                        !container.initialized) {
                        stack.pushFront(instance);
                        stack.pushFront(container);
                        continue;
                    }
                }
                if (!instance.initialized) {
                    // push the referred things on to the stack
                    // but only if they haven't yet been initialized
                    for (referredId in instance.refersTo) {
                        assert(is Id referredId);
                        value referred = instances.get(referredId);
                        if (is Partial referred,
                            !referred.initialized) {
                            stack.pushFront(referred);
                        }
                    }
                    if (instance.member,
                        is Partial container = instance.container,
                        !container.initialized) {
                        stack.pushFront(container);
                    }
                    instance.initialize<Id>(this);
                }
            } else {
                // it's an instance already, nothing to do
            }
        }
        
    }
    
    shared actual Instance reconstruct<Instance>(Id instanceId) {
        NativeDeque stack = NativeDeque();
        value root = instances.get(instanceId);
        if (!root exists) {
            if (instances.contains(instanceId)) {
                assert(is Instance r=null);
                return r;
            } else {
                throw DeserializationException("unknown id: ``instanceId``.");
            }
        }
        
        instantiateReachable(stack, instanceId);
        
        // we now have real instances for everything reachable from instanceId
        // so now we can inject the state...
        initializeReachable(stack, instanceId);
        
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

