"An implementation of `Set` that wraps an `Iterable` of
 elements. All operations on this `Set` are performed
 on the `Iterable`."
by ("Enrique Zamudio")
shared class LazySet<out Element>(elems)
        satisfies Set<Element>
        given Element satisfies Object {
    
    "The elements of the set, which must be distinct."
    {Element*} elems;
    
    clone() => this;
    
    shared actual default Integer size => elems.size;
    
    shared actual default Boolean contains(Object element) {
        for (e in elems) {
            if (element==e) {
                return true;
            }
        }
        return false;
    }
    
    iterator() => elems.iterator();
    
    shared actual Set<Element|Other&Object> union<Other>(Collection<Other> set) {
        value elems = { for (e in this) if (!e in set) e }.chain(set.coalesced);
        object union 
                extends LazySet<Element|Other&Object>(elems) {
            contains(Object key) 
                    => key in set || key in this;
        }
        return union;
    }
    
    shared actual Set<Element&Other&Object> intersection<Other>(Collection<Other> set) {
        value elems = { for (e in this) if (is Other e, e in set) e };
        object intersection 
                extends LazySet<Element&Other&Object>(elems) {
            contains(Object key) 
                    => key in set && key in this;
        }
        return intersection;
    }
    
    shared actual Set<Element|Other&Object> exclusiveUnion<Other>(Collection<Other> set) {
        value hereNotThere = { for (e in elems) if (!e in set) e };
        value thereNotHere = { for (e in set) if (exists e, !e in this) e };
        object exclusiveUnion 
                extends LazySet<Element|Other&Object>
                (hereNotThere.chain(thereNotHere)) {
            contains(Object key) 
                    => let (inThis = key in this, inThat = key in set) 
                	inThat && !inThis || inThis && !inThat;
        }
        return exclusiveUnion;
    }
    
    shared actual Set<Element> complement<Other>(Collection<Other> set) {
        value elems = { for (e in this) if (!e in set) e };
        object complement 
                extends LazySet<Element>(elems) {
            contains(Object key) 
                    => !key in set && key in this;
        }
        return complement;
    }
    
    equals(Object that) => (super of Set<Element>).equals(that);
    hash => (super of Set<Element>).hash;
    
}

