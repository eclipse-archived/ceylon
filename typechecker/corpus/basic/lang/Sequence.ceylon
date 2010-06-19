public interface Sequence<out X> 
        satisfies Correspondence<Natural, out X>, Iterable<X> {

    doc "The index of the last element of the sequence,
         or |null| if the sequence has no elements."
    public Natural? lastIndex;
    
}