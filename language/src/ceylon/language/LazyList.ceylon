doc "An implementation of List that wraps an `Iterable` of
     elements. All operations on this List are performed
     on the Iterable."
by "Enrique Zamudio"
shared class LazyList<out Element>(Element... elems)
        satisfies List<Element> {

    shared actual Integer? lastIndex {
        value c = elems.count((Element e) true);
        return c > 0 then c-1 else null;
    }

    shared actual Element? item(Integer index) {
        if (index == 0) {
            return elems.first;
        } 
        else {
            return elems.skipping(index).first;
        }
    }

    shared actual Iterator<Element> iterator {
        return elems.iterator;
    }

    doc "Returns a `List` with the elements of this
         `List` in reverse order. This operation will
         create copy the elements to a new `List`,
         so changes to the original `Iterable` will
         no longer be reflected in the new `List`."
    shared actual List<Element> reversed {
        return elems.sequence.reversed;
    }

    shared actual List<Element> clone {
        return this;
    }
    
    shared actual List<Element> span
            (Integer from, Integer? to) {
        if (exists to) {
            if (to >= from) {
                value els = from > 0 then elems.skipping(from)
                        else elems;
                return LazyList(els.taking(to-from+1)...);
            } 
            else {
                //reversed
                throw;
            }
        } 
        else {
            value els = from > 0 then elems.skipping(from)
                else elems;
            return LazyList(els...);
        }
    }
    
    shared actual List<Element> segment
            (Integer from, Integer length) {
        if (length > 0) {
            value els = from > 0 then elems.skipping(from)
                    else elems;
            return LazyList(els.taking(length)...);
        } 
        else {
            return {};
        }
    }
    
    shared actual default Boolean equals(Object that) {
        if (is List<Void> that) {
            value s = elems.count((Element e) true);
            if (that.size==s) {
                for (i in 0..s-1) {
                    value x = this[i];
                    value y = that[i];
                    if (exists x) {
                        if (exists y) {
                            if (x!=y) {
                                return false;
                            }
                        }
                        else {
                            return false;
                        }
                    }
                    else if (exists y) {
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
        variable value hash := 1;
        for (elem in elems) {
            hash *= 31;
            if (is Object elem) {
                hash += elem.hash;
            }
        }
        return hash;
    }
    
    shared default actual Element? findLast(Boolean selecting(Element elem)) {
        return elems.findLast(selecting);
    }
    
    shared actual default Element? first {
        return elems.first;
    }
    
    shared actual default Element? last {
        return elems.last;
    }
    
}
