"An implementation of `List` that wraps an `Iterable` of
 elements. All operations on this `List` are performed on 
 the `Iterable`."
by ("Enrique Zamudio")
shared class LazyList<out Element>(elems)
        satisfies List<Element> {
    
    "The elements of the list, which may have duplicates."
    {Element*} elems;
    
    shared actual Integer? lastIndex {
        value size = elems.size;
        return size > 0 then size-1;
    }
    
    shared actual Element? getFromFirst(Integer index) {
        if (index >= 0) {
            return elems.skip(index).first;
        }
        else {
            return null;
        }
    }
    
    first => elems.first;    
    last => elems.last;
    rest => LazyList(elems.rest);
    
    iterator() => elems.iterator();
    
    "Returns a `List` with the elements of this `List` 
     in reverse order. This operation will create copy 
     the elements to a new `List`, so changes to the 
     original `Iterable` will no longer be reflected in 
     the new `List`."
    shared actual List<Element> reversed 
            => elems.sequence().reversed;
    
    clone() => this;
    
    findLast(Boolean selecting(Element&Object elem)) 
            => elems.findLast(selecting);
    
    shared actual List<Element> span
            (Integer from, Integer to) {
        if (to < 0 && from < 0) {
            return [];
        }
        Integer toIndex = largest(to,0);
        Integer fromIndex = largest(from,0);
        if (toIndex >= fromIndex) {
            value els = fromIndex > 0 
                    then elems.skip(fromIndex)
                    else elems;
            return LazyList(els.take(toIndex-fromIndex+1));
        } 
        else {
            //reversed
            value seq = toIndex > 0 
                    then elems.skip(toIndex)
                    else elems;
            return seq.take(fromIndex-toIndex+1)
                    .sequence().reversed;
        }
    }
    
    spanTo(Integer to) => to < 0
            then []
            else LazyList(elems.take(to+1));
    
    spanFrom(Integer from) => from > 0
            then LazyList(elems.skip(from))
            else this;
    
    shared actual List<Element> measure
            (Integer from, Integer length) {
        if (length > 0) {
            value els = from > 0 
                    then elems.skip(from)
                    else elems;
            return LazyList(els.take(length));
        } 
        else {
            return [];
        }
    }
    
    equals(Object that) => (super of List<Element>).equals(that);
    hash => (super of List<Element>).hash;
    
}
