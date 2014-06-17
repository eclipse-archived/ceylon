"A [[Sequence]] of the given elements, or `null` if the 
 iterable is empty. A [[Sequential]] can be obtained using 
 the `else` operator:
 
     sequence(elements) else []
 "
by("Gavin")
shared [Element+]|Absent sequence<Element,Absent=Null>
        (Iterable<Element, Absent> elements)
        given Absent satisfies Null {
    if (is [Element+] elements) {
        return elements;
    }
    value array = Array(elements);
    if (array.empty) {
        assert (is Absent null);
        return null;
    }
    return ArraySequence(array);
}

"A [[Sequence]] backed by an [[Array]]. 
 
 Since `Array` is mutable, this class is private to the
 language module, where we can be sure the `Array` is not
 modified after the `ArraySequence` has been initialized."
class ArraySequence<Element>(Array<Element> array) 
        extends Object()
        satisfies [Element+] {
    
    assert (!array.empty);
    
    shared actual Element? getFromFirst(Integer index)
            => array.getFromFirst(index);
    
    shared actual Boolean contains(Object element) 
            => array.contains(element);
    
    shared actual Integer size => array.size;
    
    shared actual Iterator<Element> iterator() 
            => array.iterator();
    
    shared actual Element first {
        assert (is Element first = array.first);
        return first;
    }
    
    shared actual Element last {
        assert (is Element last = array.last);
        return last;
    }
    
    shared actual Integer lastIndex {
        assert (exists lastIndex = array.lastIndex);
        return lastIndex;
    }
    
    shared actual Element[] rest 
            => size==1 then [] else ArraySequence(Array(array.rest));
    
    shared actual [Element+] reversed 
            => ArraySequence(Array(array.reversed));
    
    shared actual Element[] segment(Integer from, Integer length) {
        if (from>lastIndex || length<=0 || from+length<=0) {
            return [];
        }
        else if (from < 0) {
            return ArraySequence(array[0:length+from]);
        } else {
            return ArraySequence(array[from:length]);
        }
    }
    
    shared actual Element[] span(Integer from, Integer to) {
        if (from <= to) {
            if (to < 0 || from > lastIndex) {
                return [];
            }
            return ArraySequence(array[largest(from, 0)..smallest(to, lastIndex)]);
        } else {
            if (from < 0 || to > lastIndex) {
                return [];
            }
            return ArraySequence(array[smallest(from, lastIndex)..largest(to, 0)]);
        }
    }
    
    shared actual Element[] spanFrom(Integer from) {
        if (from > lastIndex) {
            return [];
        } else {
            return ArraySequence(array[largest(from, 0)...]);
        }
    }
    
    shared actual Element[] spanTo(Integer to) {
        if (to < 0) {
            return [];
        } else { 
            return ArraySequence(array[...smallest(to, lastIndex)]);
        }
    }
    
}