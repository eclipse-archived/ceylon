package ceylon.language;

public interface Numeric<Other extends Numeric<Other>> 
 extends Number, Comparable<Other>, Summable<Other> {

	public Other minus(Other number);
    public Other times(Other number);
    public Other divided(Other number);
    public Other power(Other number);
    
}
