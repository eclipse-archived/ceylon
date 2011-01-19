public class RangeIterator<T>(T value, T last)
    extends RangeIteratorImpl<T>()
    given T satisfies Comparable<T>
{
    T __value() { return value; }
    T __last()  { return last; }

    public T? head () {
        if (value > last) {
            return null;
        }
        else {
            return value;
        }
    }
}
