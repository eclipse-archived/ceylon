import ceylon.language.serialization {
    Outer
}

// XXX this needs to be shared because it's used by Serializable classes
// but it's not part of the language module API
shared object outerImpl satisfies Outer {
    "The outer instance of the given member [[instance]]."
    shared actual Object referred(/*<Instance>*/Object instance) {
        return reach.getObject(instance, this);
    }
    
    shared actual String string => "Outer";
}

