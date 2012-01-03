package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Other", variance = Variance.IN,
    		satisfies="ceylon.language.Comparable<Other>"))
@SatisfiedTypes("ceylon.language.Equality")
public interface Comparable<Other extends Comparable<? super Other>> {
    
    public Comparison compare(@Name("other") Other other);
    
    /*public boolean largerThan(@Name("other") Other other); 
    
    public boolean smallerThan(@Name("other") Other other);
    
    public boolean asLargeAs(@Name("other") Other other);
    
    public boolean asSmallAs(@Name("other") Other other);*/
}
