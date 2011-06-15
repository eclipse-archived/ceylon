package ceylon.language;

public abstract class IdentifiableObject extends Object implements Equality {

   public Boolean equals(Equality that) {
        if (that instanceof IdentifiableObject ) {
            return Boolean.instance(this == that);
        }
        else {
            return _false.value;
        }
    }
    
   /* FIXME
    shared default actual Integer hash {
        return identityHash(this);
    }
    */       
    
    public String getString() {
        throw new RuntimeException(); //TODO!
    }
}