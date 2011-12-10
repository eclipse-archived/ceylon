package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@TypeParameters(@TypeParameter(value = "Other",
    		satisfies="ceylon.language.Numeric<Other>"))
@SatisfiedTypes({"ceylon.language.Number", 
		         "ceylon.language.Comparable<Other>",
		         "ceylon.language.Summable<Other>",
		         "ceylon.language.Invertable<Other>"})
public interface Numeric<Other extends Numeric<Other>> 
    extends Number, Comparable<Other>, Summable<Other>,
            Invertable<Other> {
	
	public Other minus(@Name("other") Other number);
    public Other times(@Name("other") Other number);
    public Other divided(@Name("other") Other number);
    public Other power(@Name("other") Other number);
    
    public Other getMagnitude();
    public Other getFractionalPart();
    public Other getWholePart();
    
}
