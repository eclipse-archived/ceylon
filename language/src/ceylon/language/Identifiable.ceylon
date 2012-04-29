doc "The abstract supertype of all types with a well-defined
     notion of identity. Values of type `Identifiable` may 
     be compared to determine if they are references to the 
     same object instance."
by "Gavin"
shared interface Identifiable {
    
    doc "Identity equality comparing the identity of the two 
         values. May be refined by subtypes for which value 
         equality is more appropriate."
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
         contract of `Equality`."
    see (identityHash)
    shared default actual Integer hash {
        return identityHash(this);
    }
    
}