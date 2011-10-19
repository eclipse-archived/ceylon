doc "Produces elements if an Iterable object."
see (Iterable)
by "Gavin"
shared interface Iterator<out Element> {
    
    doc "The current element."
    shared formal Element head;
    
    doc "An iterator for the remaining elements, or null if
         there are no more elements to be iterated."
    shared formal Iterator<Element>? tail;
    
}