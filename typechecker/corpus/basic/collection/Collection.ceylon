public interface Collection<out X> 
        satisfies Iterable<X>, Category {
    
    doc "The number of elements or entries belonging to the 
         collection."
    public Natural size;
    
    doc "Determine the number of times the given element
         appears in the collection."
    public Natural count(Object element);
    
    doc "Determine the number of elements or entries for
         which the given condition evaluates to |true|."
    public Natural count(Boolean having(X element));
    
    doc "Determine if the given condition evaluates to |true|
         for at least one element or entry."
    public Boolean contains(Boolean having(X element));
    
    doc "The elements of the collection, as a |Set|."
    public Set<X> elements;
    
    doc "The elements of the collection for which the given
         condition evaluates to |true|, as a |Set|."
    public Set<X> elements(Boolean having(X element));
    
    doc "The elements of the collection, sorted using the given
         comparison."
    public List<X> sortedElements(Comparison by(X x, X y));
    
    doc "An extension of the collection, with the given 
         elements. The returned collection reflects changes 
         made to the first collection."
    public Collection<T> with<T>(T... elements) given T abstracts X;

    doc "A mutable copy of the collection."
    public OpenCollection<T> copy<T>() given T abstracts X;
    
}