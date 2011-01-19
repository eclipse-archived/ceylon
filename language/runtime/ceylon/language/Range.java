package ceylon.language;

public class Range<T extends Comparable>
    extends ceylon.language.Object
    implements Iterable<T>
{
    public T first;
    public T last;

    private Range(T first, T last) {
        this.first = first;
        this.last = last;
    }

    // FIXME: it would be better for the compiler to invoke the
    // constructor directly, except that I cannot figure out how
    // to pass the type arguments correctly.
    public static <T extends Comparable> Range<T> instance(T first, T last) {
        return new Range<T>(first, last);
    }

    // FIXME: why do we need the casts to Comparable here?
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return new RangeIterator((Comparable) first, (Comparable) last);
    }
}
