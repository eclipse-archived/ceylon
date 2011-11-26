package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public abstract class IdentifiableObject extends Object implements Equality {

    public boolean equals(@Name("that") java.lang.Object that) {
        //TODO: this is broken:   
        if (that instanceof IdentifiableObject ) {
            return this == that;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public java.lang.String toString() {
        return super.toString();
    }
}