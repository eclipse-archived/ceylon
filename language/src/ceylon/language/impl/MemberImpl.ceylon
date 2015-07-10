import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.serialization {
    Member, UninitializedLateValue
}

"Implementation of [[Element]], in ceylon.language.impl because although 
 compiled user classes depend on it, it is not part of the public API."
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

