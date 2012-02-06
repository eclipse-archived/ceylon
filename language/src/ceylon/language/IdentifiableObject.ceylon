doc "The abstract supertype of all types with a well-defined
     notion of identity. Values of type `IdentifiableObject`
     may be compared to determine if they are references to
     the same object instance."
by "Gavin"
shared abstract class IdentifiableObject() 
        extends Object() {

    doc "Default definition of equality compares the 
         identity of the two values. May be refined by 
         subclasses for which value equality is more
         appropriate."
    shared default actual Boolean equals(Object that) {
        if (is IdentifiableObject that) {
            return this===that;
        }
        else {
            return false;
        }
    }
    
    doc "Default definition of `hash` returns the 
         system-defined identity hash value of the instance. 
         Subclasses which refine `equals()` must also refine 
         `hash`, according to the general contract of 
         `Equality`."
    see (identityHash)
    shared default actual Integer hash {
        return identityHash(this);
    }
    
    doc "A developer-friendly string representing the 
         instance. Default implementation concatenates the 
         name of the concrete class of the instance with the
         `hash` of the instance. Subclasses are encouraged to
         refine this implementation to produce a more 
         meaningful representation."
    shared default actual String string {
        return className(this) + "@" + hash.string;
    }
        
}