package ceylon.language;

public interface Integral<N> extends Numeric<N>, Ordinal {

    /** The binary |%| operator. */
    public N remainder(N number);
}
