package ceylon.language;

public interface Integral<Other extends Integral<Other>> 
 extends Numeric<Other>, Ordinal<Other> {

    /** The binary |%| operator. */
    public Other remainder(Other number);
}
