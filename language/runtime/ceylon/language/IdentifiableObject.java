package ceylon.language;

public abstract class IdentifiableObject extends Object implements Equality {

   public Boolean equals(Equality that) {
        if (that instanceof IdentifiableObject ) {
            return Boolean.instance(this == that);
        }
        else {
            return $false.getFalse();
        }
    }
    
   /* FIXME
    shared default actual Integer hash {
        return identityHash(this);
    }
    */       
    
    public java.lang.String toString() {
        throw new RuntimeException(); //TODO!
    }
}