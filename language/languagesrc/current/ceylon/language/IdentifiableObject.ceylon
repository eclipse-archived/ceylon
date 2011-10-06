doc "The abstract supertype of all types with a well-defined
     notion of identity. Values of type IdentifiableObject
     may be compared to determine if the are references to
     the same object instance."
by "Gavin"
shared abstract class IdentifiableObject() 
        extends Object() 
        satisfies Equality {

    doc "Default definition of equality compares the 
         identity of the two values. May be refined by 
         subclasses for which value equality is more
         appropriate."
    shared default actual Boolean equals(Equality that) {
        if (is IdentifiableObject that) {
            return this===that;
        }
        else {
            return false;
        }
    }
    
    doc "The system-defined identity hash value of the
         instance. Subclasses which refine equals must also 
         refine hash, according to the general contract of 
         Equality."
    see (identityHash)
    shared default actual Integer hash {
        return identityHash(this);
    }
    
    shared default actual String string {
        return hash.string; //TODO: improve this
    }
        
}