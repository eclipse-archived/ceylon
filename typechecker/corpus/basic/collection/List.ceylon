public interface List<out X> 
        satisfies Collection<X>, Sequence<X> {
    
    doc "The tail of the list. The returned list does 
         reflect changes to the original list."
    public List<X> rest;
    
    doc "The index of the first element of the list
         which satisfies the condition."
    throws #NotFoundException
           "if no element satsfies the condition"
    public Natural firstIndex(Boolean having(X element));

    doc "The index of the last element of the list
         which satisfies the condition."
    throws #NotFoundException
           "if no element satsfies the condition"
    public Natural lastIndex(Boolean having(X element));
    
    doc "The index of the first element of the list
         which satisfies the condition, or null if 
         no element satisfies the condition."
    public optional Natural firstIndexOrNull(Boolean having(X element));
    
    doc "The index of the last element of the list
         which satisfies the condition, or null if 
         no element satisfies the condition."
    public optional Natural lastIndexOrNull(Boolean having(X element));
    
    doc "A sublist beginning with the element at the first given 
         index up to and including the element at the second given 
         index. The size of the returned sublist is one more than 
         the difference between the two indexes. The returned list 
         does reflect changes to the original list."
    public List<X> sublist(Natural from, Natural to=lastIndex);
    
    doc "A sublist of the given length beginning with the 
         first element of the list. The returned list does 
         reflect changes to the original list."
    public List<X> leading(Natural length=1);
    
    doc "A sublist of the given length ending with the 
         last element of the list. The returned list does 
         reflect changes to the original list."
    public List<X> trailing(Natural length=1);
    
    doc "An extension of the list with the given elements
         at the end of the list. The returned list does 
         reflect changes to the original list."
    public override List<T> with<T>(T... elements) where T abstracts X;
    
    doc "An extension of the list with the given elements
         at the start of the list. The returned list does 
         reflect changes to the original list."
    public List<T> withInitial<T>(T... elements) where T abstracts X;
    
    doc "The list in reverse order. The returned list does 
         reflect changes to the original list."
    public List<X> reversed;
    
    doc "The unsorted elements of the list. The returned 
         bag does reflect changes to the original list."
    public Bag<X> unsorted;
    
    doc "A map from list index to element. The returned 
         map does reflect changes to the original list."
    public Map<Natural,X> map;
    
    doc "Produce a new list by applying an operation to
         every element of the list."
    public List<Y> transform<Y>(Y select(X element));
    
    public override OpenList<T> copy<T>() where T abstracts X;

}