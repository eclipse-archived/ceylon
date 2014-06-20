"A [[nonempty sequence|Sequence]] of the given [[elements]], 
 or  `null` if the given stream is empty. A non-null, but
 possibly empty, [[sequence|Sequential]] may be obtained 
 using the `else` operator:
 
     [Element*] sequenceOfElements = sequence(elements) else []"
by ("Gavin")
see (`function generateSequence`)
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

"Efficiently generate a [[nonempty sequence|Sequence]] of 
 the given [[size]], by starting with the given [[first]]
 element and recursively applying the given 
 [[generator function|next]].
 
     generateSequence(100, 1, (Integer last) => last*2);
 
 Hint: to generate a sequence using a function of the 
 element index, use `collect()` on a [[Range]]:
 
     (0..100).collect((Integer index) => index*index)"
throws (`class AssertionError`, "if `size<=0`")
see (`function sequence`,
     `function Sequence.collect`)
by ("Gavin")
shared [Element+] generateSequence<Element>(
    "The [[size|Sequence.size]] of the resulting sequence."
    Integer size,
    "The [[first element|Sequence.first]] of the resulting
     sequence."
    Element first,
    "A function to generate an element of the sequence, 
     given the [[previous]] generated element."
    Element next(Element previous)) {
    
    "size of the nonempty sequence must be greater than
     zero"
    assert (size>0);
    
    value array = arrayOfSize(size, first);
    variable value element = first;
    for (index in 1:size-1) {
        element = next(element);
        array.set(index, element);
    }
    return ArraySequence(array);
}


"A [[Sequence]] backed by an [[Array]]. 
 
 Since [[Array]] is mutable, this class is private to the
 language module, where we can be sure the `Array` is not
 modified after the `ArraySequence` has been initialized."
by ("Tom")
shared sealed class ArraySequence<out Element>(array) 
        extends Object()
        satisfies [Element+] {
    
    Array<Element> array;
    
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
            => size==1 then [] 
                    else ArraySequence(Array(array.rest));
    
    //TODO: return a view!
    shared actual [Element+] reversed 
            => ArraySequence(Array(array.reversed));
    
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