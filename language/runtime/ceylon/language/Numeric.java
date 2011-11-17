package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Numeric<Other extends Numeric<Other>> 
    extends Number, Comparable<Other>, Summable<Other> {
    
    public Other minus(@Name("other") Other number);
    public Other times(@Name("other") Other number);
    public Other divided(@Name("other") Other number);
    public Other power(@Name("other") Other number);
    
    public Other getMagnitude();
    public Other getFractionalPart();
    public Other getWholePart();
    
}
