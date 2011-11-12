doc "A finite set of elements which satisfy `Equality`. A set
     may not contain the same element twice."
by "Gavin"
see (OrderedSet)
shared interface Set<out Element> 
        satisfies Collection<Element> 
        given Element satisfies Equality {
    
    doc "Returns 1 if the given value belongs to this set,
         or 0 otherwise."
    shared actual Natural count(Equality element) {
        if (contains(element)) {
            return 1;
        }
        else {
            return 0;
        }
    }
    
}

doc "A set whose elements have a well-defined order."
by "Gavin"
shared interface OrderedSet<out Element>
        satisfies Set<Element> & Ordered<Element> 
        given Element satisfies Equality {}