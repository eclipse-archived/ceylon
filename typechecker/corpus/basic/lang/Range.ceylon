public class Range<X>(X first, X last) 
        satisfies Category, Sequence<X>, Case<Object>
        where X satisfies Ordinal, Comparable<X> { 
    
    doc "The first value in the range."
    public X first = first;
    
    doc "The last value in the range."
    public X last = last;
    
    doc "Return a |List| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size,
         until a value outside the range is
         reached."
    public Sequence<X> by(Natural stepSize) { throw }
    
}