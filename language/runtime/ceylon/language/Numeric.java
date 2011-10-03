package ceylon.language;

public interface Numeric<Other extends Numeric<Other>> 
 extends Number, Comparable<Other>, Summable<Other> {

    /** The binary |-| operator. */
    public Other minus(Other number);

    /** The binary |*| operator. */
    public Other times(Other number);

    /** The binary |/| operator. */
    public Other divided(Other number);

    /** The binary |**| operator. */
    public Other power(Other number);
}
