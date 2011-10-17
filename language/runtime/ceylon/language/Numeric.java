package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;

public interface Numeric<Other extends Numeric<Other>> 
 extends Number, Comparable<Other>, Summable<Other> {
	
    public Other minus(@Name("number") Other number);
    public Other times(@Name("number") Other number);
    public Other divided(@Name("number") Other number);
    public Other power(@Name("number") Other number);
    
    public Other getMagnitude();
    public Other getFractionalPart();
    public Other getWholePart();
    
}
