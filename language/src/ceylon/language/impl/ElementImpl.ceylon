import ceylon.language.serialization {
    Element
}

"Implementation of [[Element]], in ceylon.language.impl because although 
 compiled user classes depend on it, it is not part of the public API."
shared class ElementImpl(index) satisfies Element {
    shared actual Integer index;
    
    shared actual Anything referred(Object/*<Instance>*/ instance) {
        return reach.getAnything(instance, this);
    }
    
    shared actual String string
            => "Element [index=``index``]";
    
    shared actual Integer hash => index;
    shared actual Boolean equals(Object other) {
        if (is ElementImpl other) {
            return this === other || index == other.index;
        } else {
            return false;
        }
    }
}