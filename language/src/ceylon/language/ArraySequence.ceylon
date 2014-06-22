import ceylon.language { seq=sequence }
"A [[Sequence]] backed by an [[Array]]. 
 
 Since [[Array]]s are mutable, this class is private to the
 language module, where we can be sure the `Array` is not
 modified after the `ArraySequence` has been initialized."
by ("Tom")
see (`function seq`)
shared sealed class ArraySequence<out Element>(array) 
        extends Object()
        satisfies [Element+] {
    
    Array<Element> array;
    
    assert (!array.empty);
    
    getFromFirst(Integer index) => array.getFromFirst(index);
    
    contains(Object element) => array.contains(element);
    
    size => array.size;
    
    iterator() => array.iterator();
    
    shared actual Element first {
        if (exists first = array.first) {
            return first;
        }
        else {
            assert (is Element null);
            return null;
        }
    }
    
    shared actual Element last {
        if (exists last = array.last) {
            return last;
        }
        else {
            assert (is Element null);
            return null;
        }
    }
    
    rest => size==1 then [] else ArraySequence(array[1...]);
    
    clone() => ArraySequence(array.clone());
    
    shared default actual [Result+] collect<Result>(
        Result collecting(Element element)) {
        assert (nonempty sequence = array.collect(collecting));
        return sequence;
    }
    
    shared actual [Element+] sort(
        Comparison comparing(Element x, Element y)) {
        assert (nonempty sequence = array.sort(comparing));
        return sequence;
    }
    
    shared actual [Element+] reverse() {
        assert (nonempty sequence = array.reverse());
        return sequence;
    }
    
    shared actual Element[] segment(Integer from, Integer length) {
        if (from>lastIndex || length<=0 || from+length<=0) {
            return [];
        }
        else if (from < 0) {
            return ArraySequence(array[0:length+from]);
        }
        else {
            return ArraySequence(array[from:length]);
        }
    }
    
    shared actual Element[] span(Integer from, Integer to) {
        if (from <= to) {
            if (to < 0 || from > lastIndex) {
                return [];
            }
            return ArraySequence(array[from..to]);
        }
        else {
            if (from < 0 || to > lastIndex) {
                return [];
            }
            return ArraySequence(array[from..to]);
        }
    }
    
    shared actual Element[] spanFrom(Integer from) {
        if (from > lastIndex) {
            return [];
        }
        else {
            return ArraySequence(array[from...]);
        }
    }
    
    shared actual Element[] spanTo(Integer to) {
        if (to < 0) {
            return [];
        }
        else { 
            return ArraySequence(array[...to]);
        }
    }
    
}