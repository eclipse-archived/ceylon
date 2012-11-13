doc "The abstract supertype of all types with a well-defined
     notion of identity. Values of type `Identifiable` may 
     be compared using the `===` operator to determine if 
     they are references to the same object instance. For
     the sake of convenience, this interface defines a
     default implementation of value equality equivalent
     to identity. Of course, subtypes are encouraged to
     refine this implementation."
by "Gavin"
shared interface Identifiable {
    
    doc "Identity equality comparing the identity of the two 
         values. May be refined by subtypes for which value 
         equality is more appropriate. Implementations must
         respect the constraint that if `x===y` then `x==y` 
         (equality is consistent with identity)."
    shared default actual Boolean equals(Object that) {
        if (is Identifiable that) {
            return this===that;
        }
        else {
            return false;
        }
    }
    
    doc "The system-defined identity hash value of the 
         instance. Subtypes which refine `equals()` must 
         also refine `hash`, according to the general 
         contract defined by `Object`."
    see (identityHash)
    shared default actual Integer hash => identityHash(this);
    
}