import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.meta {
    type, 
    typeLiteral
}
import ceylon.language.meta.model {
    ClassModel,
    MemberClass,
    Class
}
import com.redhat.ceylon.compiler.java.runtime.model{
    TypeDescriptor
}
import com.redhat.ceylon.compiler.java.runtime.serialization {
    MemberImpl,
    ElementImpl,
    OuterImpl
}
import com.redhat.ceylon.model.typechecker.model{
    ProducedType=Type
}
import ceylon.language.serialization {
    DeserializationException
}

class DeserializationContextImpl<Id>() satisfies DeserializationContext<Id> 
        given Id satisfies Object {
    // item is either a Partial or the actual instance
    // that's not ambiguous because Partial never leaks, so it's impossible
    // for a client to use the API to instantiate a Partial
    // they can only end up in the map due to our implementation.
    NativeMap<Id,Anything> instances = NativeMapImpl<Id,Anything>();
    
    //"""a cache of "attribute" (represented as a TypeDescriptor and an attribute name)
     //  to its type"""
    shared NativeMapImpl<TypeDescriptor.Class->String, ProducedType> memberTypeCache = NativeMapImpl<TypeDescriptor.Class->String, ProducedType>();
    
    shared Anything leakInstance(Id id) => instances.get(id);
    
    shared actual void attribute(Id instanceId, ValueDeclaration attribute, Id attributeValueId) {
        attributeOrElement(instanceId, MemberImpl(attribute), attributeValueId);
    }
    
    shared DeserializationException alreadyComplete(Id instanceId) 
        => DeserializationException("instance referred to by id ``instanceId`` already complete.");
    
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
        NativeDeque deque = NativeDequeImpl();
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
                r.instantiate();
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

