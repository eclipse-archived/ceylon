doc "An implementation of Set that wraps an `Iterable` of
     elements. All operations on this Set are performed
     on the `Iterable`."
by "Enrique Zamudio"
shared class LazySet<Element>(Element... elems)
    satisfies Set<Element>
        given Element satisfies Object {

    shared actual LazySet<Element> clone {
        return LazySet(elems...);
    }
    shared actual Integer size {
        variable value c:=0;
        value sorted = elems.sort(byIncreasing((Element e) e.hash));
        if (exists l=sorted.first) {
            c:=1;
            variable Element last := l;
            for (e in sorted.rest) {
                if (e!=last) {
                    c++;
                    last:=e;
                }
            }
        }
        return c;
    }
    shared actual Iterator<Element> iterator {
        return elems.iterator;
    }

    shared actual Set<Element|Other> union<Other>(Set<Other> set)
            given Other satisfies Object {
        return LazySet(elems.chain(set)...);
    }
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
            given Other satisfies Object {
        return LazySet(for (e in set) if (is Element e) if (exists elems.find((Element o) e==o)) e);
    }
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> other)
            given Other satisfies Object {
        value hereNotThere = elements { for (e in elems) if (!e in other) e };
        value thereNotHere = elements { for (e in other) if (!exists elems.find((Element o) e==o)) e };
        return LazySet(hereNotThere.chain(thereNotHere)...);
    }
    shared actual Set<Element> complement<Other>(Set<Other> set)
            given Other satisfies Object {
        return LazySet(for (e in set) if (is Element e) if (!exists elems.find((Element o) e==o)) e);
    }

    shared actual default Boolean equals(Object that) {
        if (is Set<Object> that) {
            if (that.size==size) {
                for (element in elems) {
                    if (!element in that) {
                        return false;
                    }
                }
                else {
                    return true;
                }
            }
        }
        return false;
    }

    shared actual default Integer hash {
        variable Integer hashCode := 1;
        for(elem in elems){
            hashCode *= 31;
            hashCode += elem.hash;
        }
        return hashCode;
    }
}
