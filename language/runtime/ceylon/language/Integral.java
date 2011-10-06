package ceylon.language;

public interface Integral<Other extends Integral<Other>> 
 extends Numeric<Other>, Ordinal<Other> {
    public Other remainder(Other number);
}
