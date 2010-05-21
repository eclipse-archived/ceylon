public interface Sequence<out X> 
        satisfies Correspondence<Natural, out X>, Iterable<X> {

    doc "The index of the last element of the sequence."
    throws #EmptyException
           "if the sequence has no elements"
    public Natural lastIndex;
    
}