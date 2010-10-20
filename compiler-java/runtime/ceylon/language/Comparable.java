package ceylon.language;

public interface Comparable<T> {
    /** The binary compare operator |<=>|.  Compares this
        object with the given object. */
    public Comparison compare(T other);
}
