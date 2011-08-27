package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "T", variance = Variance.IN)
 })
public interface Comparable<T> {
    /** The binary compare operator |<=>|.  Compares this
        object with the given object. */
    public Comparison compare(T other);
    
    public boolean largerThan(T other); /* {
        return compare(other)==larger;
    }*/
    
    public boolean smallerThan(T other);/* {
        return compare(other)==smaller;
    }*/
    
    public boolean asLargeAs(T other);/* {
        return compare(other)!=smaller;
    }*/
    
    public boolean asSmallAs(T other);/* {
        return compare(other)!=larger;
    }*/
}
