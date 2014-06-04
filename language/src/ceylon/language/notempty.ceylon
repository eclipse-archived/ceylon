"A [[Sequence]] of the given elements, or `Absent` if the iterable is empty.
 A [[Sequential]] can be obtained using the `else` operator:
 
     notempty(elements) else []
 "
by("Gavin")
[Element+]|Absent notempty<Element,Absent>(Iterable<Element, Absent> elements) 
        given Absent satisfies Null {
    if (is [Element+] elements) {
        return elements;
    }
    value array = Array(elements);
    if (array.empty) {
        assert (is Absent null);
        return null;
    }
    object sequence satisfies [Element+] {
        
        shared actual Element? get(Integer index) 
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
                => notempty(array.rest) else [];
        
        shared actual [Element+] reversed {
            assert (exists reversed = /*package.*/notempty(array.reversed));
            return reversed;
        }
        
        shared actual Element[] segment(Integer from, Integer length) 
                => notempty(array[from:length]) else [];
        
        shared actual Element[] span(Integer from, Integer to) 
                => notempty(array[from..to]) else [];
        
        shared actual Element[] spanFrom(Integer from) 
                => notempty(array[from...]) else [];
        
        shared actual Element[] spanTo(Integer to) 
                => notempty(array[...to]) else [];
        
        shared actual Boolean equals(Object that) 
                => (super of Sequence<Element>).equals(that);
        
        shared actual Integer hash 
                => (super of Sequence<Element>).hash;
    
    }
    return sequence;
}