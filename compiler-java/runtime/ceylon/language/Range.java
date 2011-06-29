package ceylon.language;

public class Range<X extends Comparable<X> & Ordinal<X>> implements Iterable<X> {
    private X first;
    private X last;
    
    public Range(X first, X last) {
        this.first = first;
        this.last = last;
    }

	public Iterator<X> iterator() {
        class RangeIterator implements Iterator<X> {
        	X x;
            public RangeIterator(X x) {
            	this.x = x;
            }
            public X getHead() {
                 if (x.compare(last) == Comparison.LARGER) {
                     return null;
                 } else {
                     return x;
                 }
             }
             public Iterator<X> getTail() {
                 return new RangeIterator(x.getSuccessor());
             }
        }
        return new RangeIterator(first);
    }
}
