public mutable interface OpenIterator<X> 
        satisfies OpenIterator<X> {
    
    doc "Remove the current element from the iterable
         object to which this iterator belongs."
    public void remove();
    
    doc "Replace the current element in the iterable
         object to which this iterator belongs with
         the given object."
    public assign current;
    
}