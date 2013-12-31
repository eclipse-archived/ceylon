"An implementation of `Set` that wraps an `Iterable` of
 elements. All operations on this `Set` are performed
 on the `Iterable`."
by ("Enrique Zamudio")
shared class LazySet<out Element>({Element*} elements)
        satisfies Set<Element>
        given Element satisfies Object {
    
    clone => this;
    
    size => elements.size;
    
    iterator() => elements.iterator();
    
    shared actual Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet(elements.chain { for (e in set) if (!e in this) e });
    
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet { for (e in set) if (is Element e, e in this) e };
    
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> other)
            given Other satisfies Object {
        value hereNotThere = { for (e in elements) if (!e in other) e };
        value thereNotHere = { for (e in other) if (!e in this) e };
        return LazySet(hereNotThere.chain(thereNotHere));
    }
    
    shared actual Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet { for (e in this) if (!e in set) e };
    
    equals(Object that) => (super of Set<Element>).equals(that);
    hash => (super of Set<Element>).hash;
    
}

