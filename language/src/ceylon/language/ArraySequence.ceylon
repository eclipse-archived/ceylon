"A `Sequence` backed by an `Array`. Because `Array `is mutable this class is 
 private to the language module, where we can be sure the Array is not 
 modified after the Sequence has been initialized."
class ArraySequence<Element>(Array<Element> array) satisfies [Element+] {
    
    shared actual Element? elementAt(Integer index) 
            => array[index];
    
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
            => package.sequence(array.rest) else [];
    
    shared actual [Element+] reversed {
        assert (exists reversed = package.sequence(array.reversed));
        return reversed;
    }
    
    shared actual Element[] segment(Integer from, Integer length) {
        if (from < 0) {
            return package.sequence(array[0:length+from]) else [];
        } else {
            return package.sequence(array[from:length]) else [];
        }
    }
    
    shared actual Element[] span(Integer from, Integer to) {
        if (from <= to) {
            if (to < 0 || from > lastIndex) {
                return [];
            }
            return package.sequence(array[largest(from, 0)..smallest(to, lastIndex)]) else [];
        } else {
            if (from < 0 || to > lastIndex) {
                return [];
            }
            return package.sequence(array[smallest(from, lastIndex)..largest(to, 0)]) else [];
        }
    }
    
    shared actual Element[] spanFrom(Integer from) {
        if (from > lastIndex) {
            return [];
        } else {
            return package.sequence(array[largest(from, 0)...]) else [];
        }
    }
    
    shared actual Element[] spanTo(Integer to) {
        if (to < 0) {
            return [];
        } else { 
            return package.sequence(array[...smallest(to, lastIndex)]) else [];
        }
    }
    
    shared actual Boolean equals(Object that) 
            => (super of Sequence<Element>).equals(that);
    
    shared actual Integer hash 
            => (super of Sequence<Element>).hash;
    
}