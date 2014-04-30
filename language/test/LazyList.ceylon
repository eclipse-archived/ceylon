"An implementation of `List` that wraps an `Iterable` of
 elements. All operations on this `List` are performed on 
 the `Iterable`."
by ("Enrique Zamudio")
deprecated
shared class LazyList<out Element>(elements)
        satisfies List<Element> {
    
    "The elements of the list, which may have duplicates."
    {Element*} elements;
    
    shared actual Integer? lastIndex {
        value size = elements.size;
        return size > 0 then size-1;
    }
    
    shared actual Element? get(Integer index) {
        if (index >= 0) {
            return elements.skip(index).first;
        }
        else {
            return null;
        }
    }
    
    first => elements.first;    
    last => elements.last;
    rest => LazyList(elements.rest);
    
    iterator() => elements.iterator();
    
    "Returns a `List` with the elements of this `List` 
     in reverse order. This operation will create copy 
     the elements to a new `List`, so changes to the 
     original `Iterable` will no longer be reflected in 
     the new `List`."
    shared actual List<Element> reversed 
            => elements.sequence.reversed;
    
    clone() => this;
    
    findLast(Boolean selecting(Element elem)) 
            => elements.findLast(selecting);
    
    shared actual List<Element> span
            (Integer from, Integer to) {
        if (to < 0 && from < 0) {
            return [];
        }
        Integer toIndex = largest(to,0);
        Integer fromIndex = largest(from,0);
        if (toIndex >= fromIndex) {
            value els = fromIndex > 0 
                    then elements.skip(fromIndex)
                    else elements;
            return LazyList(els.take(toIndex-fromIndex+1));
        } 
        else {
            //reversed
            value seq = toIndex > 0 
                    then elements.skip(toIndex)
                    else elements;
            return seq.take(fromIndex-toIndex+1)
                    .sequence.reversed;
        }
    }
    
    spanTo(Integer to) => to < 0
            then []
            else LazyList(elements.take(to+1));
    
    spanFrom(Integer from) => from > 0
            then LazyList(elements.skip(from))
            else this;
    
    shared actual List<Element> segment
            (Integer from, Integer length) {
        if (length > 0) {
            value els = from > 0 
                    then elements.skip(from)
                    else elements;
            return LazyList(els.take(length));
        } 
        else {
            return [];
        }
    }
    
    equals(Object that) => (super of List<Element>).equals(that);
    hash => (super of List<Element>).hash;
    
}
