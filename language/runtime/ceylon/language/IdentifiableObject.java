package ceylon.language;

public abstract class IdentifiableObject extends Object implements Equality {

   public boolean equals(java.lang.Object that) {
        if (that instanceof IdentifiableObject ) {
            return this == that;
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        throw new RuntimeException(); //TODO!
    }
    
    public java.lang.String toString() {
        throw new RuntimeException(); //TODO!
    }
}