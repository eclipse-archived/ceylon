public mutable interface OpenList<X> 
        satisfies List<X>, OpenCollection<X>, OpenSequence<X> {

    doc "Remove the element at the given index, decrementing
         the index of every element with an index greater
         than the given index by one. Return the removed
         element."
    public X removeIndex(Natural at);
    
    doc "Add the given elements at the end of the list."
    public void prepend(X... elements);
    
    doc "Add the given elements at the start of the list,
         incrementing the index of every existing element
         by the number of given elements."
    public void append(X... elements);

    doc "Insert the given elements beginning at the given
         index, incrementing the index of every existing 
         element with that index or greater by the number 
         of given elements."
    public void insert(Natural at, X... elements);
    
    doc "Remove elements beginning with the first given index 
         up to and including the second given index,
         decrementing the indexes of all elements after
         with the second given index by one more than the 
         difference between the two indexes."
    public void delete(Natural from, Natural to=lastIndex);
    
    doc "Remove and return the first element, decrementing
         the index of every other element by one."
    throws #EmptyException
           "if the list is empty"
    public X removeFirst();

    doc "Remove and return the last element."
    throws #EmptyException
           "if the list is empty"
    public X removeLast();
    
    doc "Reverse the order of the list."
    public void reverse();
    
    doc "Reorder the elements of the list, according to the 
         given comparison."
    public void resort(Comparison by(X x, X y));
    
    override public OpenList<X> rest;
    override public OpenList<X> leading(Natural length);
    override public OpenList<X> trailing(Natural length);
    override public OpenList<X> sublist(Natural from, Natural to);
    override public OpenList<X> reversed;
    override public OpenMap<Natural,X> map;
    
}