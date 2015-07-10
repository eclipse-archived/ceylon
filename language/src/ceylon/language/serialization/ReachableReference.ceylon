import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.serialization {
    reach
}
"A means via which one instance can refer to another."
shared interface ReachableReference/*<Instance>*/ // Reachable
        of Member|Element|Outer {
    "The [[referred]] instance reachable from the given [[instance]]."
    shared formal Anything referred(Object/*<Instance>*/ instance);
}

shared abstract class UninitializedLateValue() of uninitializedLateValue {}
shared object uninitializedLateValue extends UninitializedLateValue() {}

"An instance referring to another instance via a reference attribute."
shared sealed interface Member // or Reference
        satisfies ReachableReference {
    "The attribute making the reference."
    shared formal ValueDeclaration attribute;
    "The [[referred]] instance reachable from the given [[instance]]."
    shared actual formal Anything|UninitializedLateValue referred(Object/*<Instance>*/ instance);
    
}

shared class MemberImpl(attribute) satisfies Member {
    shared actual ValueDeclaration attribute;
    
    shared actual Anything|UninitializedLateValue referred(Object/*<Instance>*/ instance) {
        return reach.getAnything(instance, this);
    }
    
    shared actual String string
        => "Member [attribute=``attribute``]";
    
    shared actual Integer hash => attribute.hash;
    shared actual Boolean equals(Object other) {
        if (is MemberImpl other) {
            return this === other || attribute == other.attribute;
        } else {
            return false;
        }
    }
}


"An [[Array]] instance referring to another instance via one 
 of its elements."
shared sealed interface Element /*<Instance>*/
        satisfies ReachableReference /*<Instance>*/{
    "The index of the element in the Array which makes the reference."
    shared formal Integer index;
}

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

"A member instance referring to its outer instance."
shared sealed interface Outer /*<Instance>*/
        satisfies ReachableReference/*<Instance>*/ {
    "The outer instance of the given member [[instance]]."
    shared actual formal Object referred(/*<Instance>*/Object instance);
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

