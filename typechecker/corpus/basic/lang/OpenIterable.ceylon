public mutable interface OpenIterable<X> 
        satisfies Iterable<X> {
    
    public override OpenIterator<X> iterator();
    
}