public class Range<X>(X first, X last) 
        satisfies Sequence<X>, Case<Object>
        where X>=Ordinal & X>=Comparable<X> { 
    
    doc "The first value in the range."
    public X first = first;
    
    doc "The last value in the range."
    public X last = last;
    
    doc "Return a |List| of values in the range, 
         beginning at the first value, and 
         incrementing by a constant step size,
         until a value outside the range is
         reached."
    public List<X> by(Natural stepSize) { throw }
    
}