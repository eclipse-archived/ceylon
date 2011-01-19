package ceylon.language;

public class Range<T extends Comparable>
    extends ceylon.language.Object
    implements Iterable<T>
{
    public T first;
    public T last;

    public Range(T first, T last) {
        this.first = first;
        this.last = last;
    }

    // FIXME: why do we need the casts to Comparable here?
    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return new RangeIterator((Comparable) first, (Comparable) last);
    }
}
