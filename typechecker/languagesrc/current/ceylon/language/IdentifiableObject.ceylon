shared abstract class IdentifiableObject() 
        extends Object() 
        satisfies Equality<IdentifiableObject> {

    shared default actual Boolean equals(IdentifiableObject that) {
        return this===that;
    }
    
    shared default actual Integer hash {
        return identityHash(this);
    }
        
}