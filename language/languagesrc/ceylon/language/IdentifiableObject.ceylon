shared abstract class IdentifiableObject() 
        extends Object() 
        satisfies Equality<IdentifiableObject> {

    doc "The |Class| of the instance."
    shared actual Class<subtype> type {
        throw;
    }
    
    shared default actual Boolean equals(IdentifiableObject that) {
        return this===that;
    }
    
    shared default actual Integer hash {
        return identityHash(this);
    }
        
}