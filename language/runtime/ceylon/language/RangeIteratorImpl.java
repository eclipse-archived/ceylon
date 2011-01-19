package ceylon.language;

public abstract class RangeIteratorImpl<T extends Comparable>
    extends ceylon.language.Object
    implements Iterator<T>
{
    protected abstract T __value();
    protected abstract T __last();

    // FIXME: this method assumes that T is a Natural.
    // Obviously this isn't necessarily the case!
    // This should all be written in Ceylon, and this
    // entire class removed.
    @SuppressWarnings("unchecked")
    public Iterator<T> tail() {
        return new RangeIterator(
            ((Natural) __value()).plus(Natural.instance(1)),
            __last());
    }
}
