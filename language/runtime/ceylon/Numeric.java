package ceylon;

public interface Numeric<N> extends Number, Comparable<N> {

    /** The binary |+| operator. */
    public Numeric plus(Numeric number); // XXX not as spec

    /** The binary |-| operator. */
    public Numeric minus(Numeric number); // XXX not as spec

    /** The binary |*| operator. */
    public Numeric times(Numeric number); // XXX not as spec

    /** The binary |/| operator. */
    public Numeric divided(Numeric number); // XXX not as spec

    /** The binary |**| operator. */
    public Numeric power(Numeric number); // XXX not as spec
}
