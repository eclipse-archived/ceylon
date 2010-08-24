package ceylon;

public interface Integral<N> extends Numeric<N>, Ordinal {

    /** The binary |%| operator. */
    public Integral remainder(Integral integral); // XXX not as spec
}
