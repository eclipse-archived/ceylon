package ceylon.language;

public abstract class IdentifiableObject extends Object /* FIXME: implements Equality */ {

/* FIXME:
   shared default actual Boolean equals(Equality that) {
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
 */       
}