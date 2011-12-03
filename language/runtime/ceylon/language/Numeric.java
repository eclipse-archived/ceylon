package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.TypeParameter;
import com.redhat.ceylon.compiler.metadata.java.TypeParameters;

@Ceylon
@TypeParameters({
    @TypeParameter(value = "Other",
    		satisfies="ceylon.language.Numeric<Other>")
})
public interface Numeric<Other extends Numeric<Other>> 
    extends Number, Comparable<Other>, Summable<Other> {
    
    public Other times(@Name("other") Other number);
    public Other divided(@Name("other") Other number);
    public Other power(@Name("other") Other number);
    
    public Other getMagnitude();
    public Other getFractionalPart();
    public Other getWholePart();
    
}
