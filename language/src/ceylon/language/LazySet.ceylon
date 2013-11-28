"An implementation of `Set` that wraps an `Iterable` of
 elements. All operations on this `Set` are performed
 on the `Iterable`."
by ("Enrique Zamudio")
shared class LazySet<out Element>({Element*} elements)
        satisfies Set<Element>
        given Element satisfies Object {
    
    clone => this;
    
    shared actual Integer size {
        variable value c=0;
        value sorted = elements.sort(byIncreasing(Object.hash));
        if (exists l=sorted.first) {
            c=1;
            variable Element last = l;
            for (e in sorted.rest) {
                if (e!=last) {
                    c++;
                    last=e;
                }
            }
        }
        return c;
    }
    
    shared actual Iterator<Element> iterator() {
        object iterator satisfies Iterator<Element> {
            value sorted = elements.sort(byIncreasing(Object.hash)).iterator();
            variable Element|Finished ready = sorted.next();
            shared actual Element|Finished next() {
                Element|Finished next = ready;
                if (!is Finished next) {
                    while (next==ready) {
                        ready = sorted.next();
                    }
                }
                return next; 
            }
        }
        return iterator;
    }
    
    shared actual Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object 
            => LazySet(elements.chain(set));
    
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
