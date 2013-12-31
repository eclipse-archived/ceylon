"An implementation of `Set` that wraps an `Iterable` of
 elements. All operations on this `Set` are performed
 on the `Iterable`."
by ("Enrique Zamudio")
shared class LazySet<out Element>(elements)
        satisfies Set<Element>
        given Element satisfies Object {
    
    "The elements of the set, which must be distinct."
    {Element*} elements;
    
    clone => this;
    
    shared actual default Integer size => elements.size;
    
    iterator() => elements.iterator();
    
    shared actual Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet({ for (e in this) if (!e in set) e }.chain(set));
    
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet { for (e in this) if (is Other e, e in set) e };
    
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object {
        value hereNotThere = { for (e in elements) if (!e in set) e };
        value thereNotHere = { for (e in set) if (!e in this) e };
        return LazySet(hereNotThere.chain(thereNotHere));
    }
    
    shared actual Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet { for (e in this) if (!e in set) e };
    
    equals(Object that) => (super of Set<Element>).equals(that);
    hash => (super of Set<Element>).hash;
    
}

