import ceylon.language.meta.model {
    ClassModel
}

"Holds state for reconstructing an instance."
abstract class Partial(id) {
    
    "The id"
    shared Object id;
    
    "The class, if we know it yet"
    shared variable ClassModel? clazz = null;
    
    "The containing instance (a partial for it, or the instance itself), or null"
    shared variable Anything container = null;
    
    "The (partially initialized) instance, if it has been instantiated yet, or null."
    shared variable Anything instance_ = null;
    
    "The state, mapping references to the 
     id of the corresponding value in the [[DeserializationContext]]."
    shared variable NativeMap<ReachableReference, Object>? state = NativeMapImpl<ReachableReference, Object>();
    
    "Add some state."
    shared void addState(ReachableReference attrOrIndex, Object partialOrComplete) {
        assert(exists s=state);
        s.put(attrOrIndex, partialOrComplete);
    }
    //shared formal void addState(String|Integer attrOrIndex, Object partialOrComplete);
    
    "Creates and initializes the [[instance_]] using backend-specific reflection."
    throws(`class DeserializationException`,
        "* the class of the instance has already been specified
         * instance is a member instance and the container has not been specified")
    shared formal void instantiate();
    
    "Initializes the [[instance_]] using backend-specific reflection, then sets the state to null."
    throws(`class DeserializationException`,
        "the partial contains insufficient state")
    shared formal void initialize<Id>(DeserializationContextImpl<Id> context)
            given Id satisfies Object;
    
    "Whther the partial has been instantiated"
    shared Boolean instantiated => instance_ exists;
    "Whther the partial has been initialized"
    shared Boolean initialized => !state exists;
    "Whther the partial is for an instance of a member class"
    shared Boolean member => container exists;
    
    shared Anything instance() {
        assert(instantiated && initialized);
        return instance_;
    }
    
    "The ids of the instances that this instance refers to"
    shared {Object*} refersTo {
        assert(exists s=state);
        return s.items;
    }
}
