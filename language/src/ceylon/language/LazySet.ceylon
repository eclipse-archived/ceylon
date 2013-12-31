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
    
    shared actual default Boolean contains(Object element) {
        for (e in elements) {
            if (element==e) {
                return true;
            }
        }
        return false;
    }
    
    iterator() => elements.iterator();
    
    shared actual Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object {
        value elements = { for (e in this) if (!e in set) e }.chain(set);
        object union 
                extends LazySet<Element|Other>(elements) {
            shared actual Boolean contains(Object key) 
                    => key in set || key in this;
        }
        return union;
    }
    
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object {
        value elements = { for (e in this) if (is Other e, e in set) e };
        object intersection 
                extends LazySet<Element&Other>(elements) {
            shared actual Boolean contains(Object key) 
                    => key in set && key in this;
        }
        return intersection;
    }
    
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
            given Other satisfies Object {
        value hereNotThere = { for (e in elements) if (!e in set) e };
        value thereNotHere = { for (e in set) if (!e in this) e };
        object exclusiveUnion 
                extends LazySet<Element|Other>
                (hereNotThere.chain(thereNotHere)) {
            shared actual Boolean contains(Object key) {
                Boolean inThis = key in this;
                return key in set then !inThis else inThis;
            }
        }
        return exclusiveUnion;
    }
    
    shared actual Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object {
        value elements = { for (e in this) if (!e in set) e };
        object complement 
                extends LazySet<Element>(elements) {
            shared actual Boolean contains(Object key) 
                    => !key in set && key in this;
        }
        return complement;
    }
    
    equals(Object that) => (super of Set<Element>).equals(that);
    hash => (super of Set<Element>).hash;
    
}

