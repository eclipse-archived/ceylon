package ceylon.language;

public interface Invertable<I> {

    /** The unary |+| operator. */
    public I getPositiveValue();

    /** The unary |-| operator. */
    public I getNegativeValue();
}
