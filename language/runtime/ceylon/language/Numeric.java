package ceylon.language;

public interface Numeric<N> extends Number, Comparable<N> {

    /** The binary |+| operator. */
    public N plus(N number);

    /** The binary |-| operator. */
    public N minus(N number);

    /** The binary |*| operator. */
    public N times(N number);

    /** The binary |/| operator. */
    public N divided(N number);

    /** The binary |**| operator. */
    public N power(N number);
}
