public class Range<X>(X first, X last) satisfies Iterable<X>
    given X satisfies Ordinal<X>, Comparable<X> {

    Iterator<X> iterator() {
        class RangeIterator(X x) satisfies Iterator<X> {
             X? head() {
                 if (x > last) {
                     return null;
                 }
                 else {
                     return x;
                 }
             }
             Iterator<X> tail() {
                 return RangeIterator(x.successor());
             }
        }
        return RangeIterator(first)
    }
}
