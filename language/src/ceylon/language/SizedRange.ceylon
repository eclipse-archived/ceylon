"A range constructed from a first element,
 which is [[Enumerable]], and a positive size.
 
 The _segment_ operator `:` is an abbreviation for
 `SizedRange` instantiation if the size is positive.
 
     for (i in start:size) { ... }
     for (char in '0':10) { ... }"
see (`class Range`, `interface Enumerable`)
shared class SizedRange<Element>(first, size) 
        extends Object() 
        satisfies [Element+]
        given Element satisfies Enumerable<Element> {

    "The start of the range."
    shared actual Element first;
    "The size of the range."
    shared actual Integer size;
    
    "Can't be used for empty segments"
    assert (size > 0);
    
    shared actual String string 
            => first.string + ":" + size.string;
    
    shared actual Element last
            => first.neighbour(size-1);
    
    "Determines if this sized range has more elements than the given [[length]].
     As the [[size]] is known, this is a cheap operation."
    shared actual Boolean longerThan(Integer length)
            => size > length;
    
    "Determines if this sized range has less elements than the given [[length]].
     As the [[size]] is known, this is a cheap operation."
    shared actual Boolean shorterThan(Integer length)
            => size < length;
    
    "The index of the end of the sized range."
    shared actual Integer lastIndex => size-1; 
    
    "The rest of the sized range, without the start of the sized range."
    shared actual Element[] rest 
            => size==1 then [] else SizedRange(first.successor, size-1);

    shared actual [Element+] reversed 
            => ArraySequence(Array(this)).reversed; //TODO: return a view
    
    
    "The element of the sized range that occurs [[index]] values
     after the start of the sized range. Note that, depending on
     [[Element]]'s [[neighbour|Enumerable.neighbour]] implementation,
     this operation may be inefficient for large ranges."
    shared actual Element? getFromFirst(Integer index) {
        if (index<0 || index > size-1) {
            return null;
        }
        return first.neighbour(index);
    }
    
    "An iterator for the elements of the sized range."
    shared actual Iterator<Element> iterator() {
        object iterator
                satisfies Iterator<Element> {
            variable value count = 0;
            variable value current = first;
            shared actual Element|Finished next() {
                if (++count>size) {
                    return finished;
                }
                else {
                    return current++;
                } 
            }
            string => outer.string + ".iterator()"; // TODO don't we need to wrap outer.string in parentheses?
        }
        return iterator;
    }
    
    shared actual {Element+} by(Integer step) {
        "step size must be greater than zero"
        assert (step > 0);
        return step == 1 then this else By(step);
    }
    
    class By(Integer step)
            satisfies {Element+} {
        
        size => 1 + (outer.size - 1) / step;
        
        first => outer.first;
        
        string => "``outer.string``.by(``step``)"; // TODO parens around outer.string?
        
        shared actual Iterator<Element> iterator() {
            object iterator
                    satisfies Iterator<Element> {
                variable value count = 0;
                variable value current = first;
                shared actual Element|Finished next() {
                    if (++count>size) { // refers to the inner size, which accounts for step
                        return finished;
                    }
                    else {
                        value result = current;
                        current = current.neighbour(step);
                        return result;
                    } 
                }
                string => outer.string + ".iterator()";
            }
            return iterator;
        }
    }
    
    "Returns a sized range of the same size and type as this
     sized range, with its starting point shifted by the given number 
     of elements, where:
     
     - a negative [[shift]] measures 
       [[decrements|Ordinal.predecessor]], and 
     - a positive `shift` measures 
       [[increments|Ordinal.successor]]."
    shared SizedRange<Element> shifted(Integer shift) {
        if (shift==0) {
            return this;
        }
        else {
            value start = first.neighbour(shift);
            return SizedRange(start, size);
        }
    }
    
    "Determines if this sized range includes the given object."
    shared actual Boolean contains(Object element) {
        if (is Element element) {
            return containsElement(element);
        }
        else {
            return false;
        }
    }
    
    "Determines if this sized range includes the given value."
    shared actual Boolean occurs(Anything element) {
        if (is Element element) {
            return containsElement(element);
        }
        else {
            return false;
        }
    }
    
    "Determines if this sized range includes the given value."
    shared Boolean containsElement(Element x)
            => x.offset(first) < size;
    
    shared actual Boolean includes(List<Anything> sublist) {
        if (sublist.empty) {
            return true;
        }
        else if (is SizedRange<Element> sublist) {
            return includesSizedRange(sublist);
        }
        else {
            return super.includes(sublist);
        }
    }
    
    "Determines if this sized range includes the given sized range."
    shared Boolean includesSizedRange(SizedRange<Element> sublist) {
        value offset = sublist.first.offset(first);
        return offset >= 0
                && offset + sublist.size <= size;
    }
    
    "Efficiently determines if two sized ranges are the same
     by comparing their sizes and start points."
    shared actual Boolean equals(Object that) {
        if (is SizedRange<Object> that) {
            //optimize for another Range
            return that.size==size && that.first==first;
        }
        else {
            //it might be another sort of List
            return super.equals(that);
        }
    }
    
    "Returns the sized range itself, since sized ranges are 
     immutable."
    shared actual SizedRange<Element> clone() => this;
    
    "Returns the sized range itself, since a sized range cannot
     contain nulls."
    shared actual SizedRange<Element> coalesced => this;
    
    "Returns this sized range."
    shared actual SizedRange<Element> sequence() => this;
    
    shared actual Element[] segment(Integer from, Integer length)
            => length<=0 then [] else SizedRange(first.neighbour(from), from+length < size then length else size-from);
    
    shared actual Element[] span(Integer from, Integer to) {
        if (from<=to) {
            if (to<0 || from>size) {
                return [];
            }
            else {
                return SizedRange(first.neighbour(from), to < size then to-from+1 else size-from);
            }
        }
        else {
            if (to>size || from<0) {
                return [];
            }
            else {
                value sizedRange = SizedRange(first.neighbour(from), to < size then to-from+1 else size-from);
                return sizedRange.reversed;
            }
        }
    }
    
    shared actual Element[] spanFrom(Integer from) {
        if (from <= 0) {
            return this;
        }
        else if (from < size) {
            return SizedRange(first.neighbour(from), size-from);
        }
        else {
            return [];
        }
    }
    
    shared actual Element[] spanTo(Integer to) {
        if (to >= size-1) {
            return this;
        }
        else if (to >= 0) {
            return SizedRange(first, to);
        }
        else {
            return [];
        }
    }
}