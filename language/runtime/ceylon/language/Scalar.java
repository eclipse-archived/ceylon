package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value = "Other",
    		satisfies="ceylon.language.Scalar<Other>"))
@SatisfiedTypes({"ceylon.language.Number",
		         "ceylon.language.Numeric<Other>",
		         "ceylon.language.Comparable<Other>"})
public interface Scalar<Other extends Scalar<Other>> 
    extends Number, Numeric<Other>, Comparable<Other> {
	    
    public Other getMagnitude();
    public Other getFractionalPart();
    public Other getWholePart();
    
}
