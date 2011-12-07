package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;
import com.redhat.ceylon.compiler.metadata.java.Variance;

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
