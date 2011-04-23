shared abstract class IdentifiableObject() 
        extends Object() 
        satisfies Equality {

    shared default actual Boolean equals(Object that) {
        if (is IdentifiableObject that) {
            return this===that;
        }
        else {
            return false;
        }
    }
    
    shared default actual Integer hash {
        return identityHash(this);
    }
    
    shared default actual String string {
        throw; //TODO!
    }
        
}