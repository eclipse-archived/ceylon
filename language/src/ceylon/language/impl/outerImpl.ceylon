import ceylon.language.serialization {
    Outer
}

"Implementation of [[Element]], in ceylon.language.impl because although 
 compiled user classes depend on it, it is not part of the public API."
shared object outerImpl satisfies Outer {
    "The outer instance of the given member [[instance]]."
    shared actual Object referred(/*<Instance>*/Object instance) {
        return reach.getObject(instance, this);
    }
    
    shared actual String string => "Outer";
}

