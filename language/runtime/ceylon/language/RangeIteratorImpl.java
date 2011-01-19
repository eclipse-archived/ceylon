package ceylon.language;

public abstract class RangeIteratorImpl<T extends Comparable>
    extends ceylon.language.Object
    implements Iterator<T>
{
    protected abstract T __value();
    protected abstract T __last();

    // FIXME: this should all be written in Ceylon,
    // and this entire class removed.
    @SuppressWarnings("unchecked")
    public Iterator<T> tail() {
        return new RangeIterator(
            ((Ordinal<T>) __value()).successor(),
            __last());
    }
}
