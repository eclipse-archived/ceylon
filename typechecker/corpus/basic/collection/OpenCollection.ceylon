public mutable interface OpenCollection<X> 
        satisfies OpenIterable<X>, OpenCategory<X>, Collection<X> {
    
    doc "Remove all elements or entries of the collection,
         resulting in an empty collection."
    public Boolean clear();
    
    doc "Remove the given elements from the collection.
         Return the number of elements which belonged
         to the collection."
    public Natural remove(X... elements);
    
    doc "Remove all elements from the collection for which
         the given condition evaluates to |true|. Return 
         the number of elements which were removed."
    public Natural remove(Boolean having(X element));
    
}