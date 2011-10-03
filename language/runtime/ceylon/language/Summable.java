package ceylon.language;

public interface Summable<Other extends Summable<Other>> {

    /** The binary |+| operator. */
    public Other plus(Other number);
}
