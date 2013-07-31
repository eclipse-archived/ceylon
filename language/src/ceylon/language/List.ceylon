"""Represents a collection in which every element has a 
   unique non-negative integer index.
   
   A `List` is a `Collection` of its elements, and a 
   `Correspondence` from indices to elements.
   
   Direct access to a list element by index produces a
   value of optional type. The following idiom may be
   used instead of upfront bounds-checking, as long as 
   the list element type is a non-`null` type:
   
       value char = "hello world"[index];
       if (exists char) { /*do something*/ }
       else { /*out of bounds*/ }
   
   To iterate the indexes of a `List`, use the following
   idiom:
   
       for (i->char in "hello world".indexed) { ... }"""
see (`Sequence`, `Empty`, `Array`)
shared interface List<out Element>
        satisfies Collection<Element> &
                  Correspondence<Integer,Element> &
                  Ranged<Integer,List<Element>> &
                  Cloneable<List<Element>> {
    
    "The index of the last element of the list, or
     null if the list is empty."
    see (`size`)
    shared formal Integer? lastIndex;
    
    "The number of elements in this sequence, always
     `sequence.lastIndex+1`."
    see (`lastIndex`)
    shared actual default Integer size => (lastIndex else -1) + 1;
    
    shared actual default Boolean shorterThan(Integer length) 
            => size<length;
    
    shared actual default Boolean longerThan(Integer length) 
            => size>length;
    
    "The rest of the list, without the first element."
    shared actual formal List<Element> rest;
    
    "Determines if the given index refers to an element
         of this sequence, that is, if
         `index<=sequence.lastIndex`."
    shared actual default Boolean defines(Integer index) => 
            index <= (lastIndex else -1);
	
    "Returns the element of this sequence with the given
         index, or `null` if the given index is past the end
         of the sequence, that is, if
         `index>sequence.lastIndex`. The first element of
         the sequence has index `0`."
    shared actual formal Element? get(Integer index);
    
    shared actual default Iterator<Element> iterator() {
        object listIterator
                satisfies Iterator<Element> {
            variable Integer index = 0;
            shared actual Element|Finished next() {
                if (index <= (lastIndex else -1)) {
                    assert (is Element elem = outer.get(index++));
                    return elem;
                }
                else {
                    return finished;
                }
            }
        }
        return listIterator;
    }
    
    "Reverse this list, returning a new list."
    shared formal List<Element> reversed;
    
    "Two `List`s are considered equal iff they have the 
     same `size` and _entry sets_. The entry set of a 
     list `l` is the set of elements of `l.indexed`. 
     This definition is equivalent to the more intuitive 
     notion that two lists are equal iff they have the 
     same `size` and for every index either:
     
     - the lists both have the element `null`, or
     - the lists both have a non-null element, and the
       two elements are equal."
    shared actual default Boolean equals(Object that) {
        if (is List<Anything> that) {
            if (that.size==size) {
                for (i in 0..size-1) {
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
        variable value hash = 1;
        for (elem in this) {
            hash *= 31;
            if (exists elem) {
                hash += elem.hash;
            }
        }
        return hash;
    }
    
    shared default actual Element? findLast(
            Boolean selecting(Element elem)) {
        if (exists l=lastIndex) {
            variable value index = l;
            while (index >= 0) {
                if (exists elem = this[index--]) {
                    if (selecting(elem)) {
                        return elem;
                    }
                }
            }
        }
        return null;
    }
    
    "Returns the first element of this `List`, if any."
    shared actual default Element? first => this[0];
    
    "Returns the last element of this `List`, if any."
    shared actual default Element? last {
        if (exists i = lastIndex) {
            return this[i];
        }
        return null;
    }
    
    "Returns a new `List` that starts with the specified
     element, followed by the elements of this `List`."
    see (`following`)
    shared default [Element|Other+] withLeading<Other>(
            "The first element of the resulting sequence."
            Other element) {
        value sb = SequenceBuilder<Element|Other>();
        sb.append(element);
        if (!empty) {
            sb.appendAll(this);
        }
        assert (nonempty seq=sb.sequence);
        return seq;
    }
    
    "Returns a new `List` that contains the specified
     element appended to the end of the elements of this 
     `List`."
    shared default [Element|Other+] withTrailing<Other>(
            "The last element of the resulting sequence."
            Other element) {
        value sb = SequenceBuilder<Element|Other>();
        if (!empty) {
            sb.appendAll(this);
        }
        sb.append(element);
        assert (nonempty seq=sb.sequence);
        return seq;
    }
    
    "Determine if this list occurs at the start of the 
     given list."
    shared default Boolean occursAtStart(List<Anything> list)
            => occursAt(0, list);
    
    "Determine if this list occurs at the given index 
     in the given list."
    shared default Boolean occursAt(
            "The index at which this list might occur"
            Integer index, 
            List<Anything> list) {
        for (i in index:size) {
            value x = this[i];
            value y = list[i];
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
    
    "Determine if this list occurs as a sublist of the 
     given list."
    shared default Boolean occursIn(List<Anything> list) {
        for (i in 0:list.size) {
            if (occursAtStart(list[i...])) {
                return true;
            }
        }
        return false;
    }
    
    "The indexes in the given list at which this list 
     occurs."
    shared default {Integer*} occurrencesIn(List<Anything> list) =>
            { for (index in 0:list.size) 
                    if (occursAt(index,list)) index };
    
    "The indexes in this list for which the element 
     satisfies the given predicate."
    shared default {Integer*} indexes(
            "The predicate the indexed elements must satisfy"
            Boolean selecting(Element element)) =>
            { for (index in 0:size) 
                    //TODO: fix this awful hack
                    if (selecting(this[index] else nothing)) index };
    
    shared default List<Element> trim(Boolean trimming(Element elem)) {
        if (exists l=lastIndex) {
            variable Integer from=-1;
            variable Integer to=-1;
            for (index in 0..l) {
                if (!trimming(this[index] else nothing)) {
                    from = index;
                    break;
                }
            }
            else {
                return [];
            }
            for (index in l..0) {
                if (!trimming(this[index] else nothing)) {
                    to = index;
                    break;
                }
            }
            else {
                return [];
            }
            return this[from:to];
        }
        else {
            return [];
        }
    }
    
    shared default List<Element> trimLeading(Boolean trimming(Element elem)) {
        if (exists l=lastIndex) {
            for (index in 0..l) {
                if (!trimming(this[index] else nothing)) {
                    return this[index:l];
                }
            }
        }
        return [];
    }
    
    shared default List<Element> trimTrailing(Boolean trimming(Element elem)) {
        if (exists l=lastIndex) {
            for (index in l..0) {
                if (!trimming(this[index] else nothing)) {
                    return this[0:index];
                }
            }
        }
        return [];
    }
    
    "Select the first elements of this list, returning a 
     list no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length."
    shared default List<Element> initial(Integer length)
            => this[0:length];
    
    "Select the last elements of the list, returning a list 
     no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length."
    shared default List<Element> terminal(Integer length) {
        if (exists l = lastIndex, length>0) {
            return this[l-length+1..l];
        }
        else {
            return [];
        }
    }
    
    /*"Select the elements between the given indices. If 
         the start index is the same as the end index,
         return a list with a single element. If the start 
         index is larger than the end index, return the
         elements in the reverse order from the order in
         which they appear in this list. If both the
         start index and the end index are larger than the
         last index in the list, return an empty list. 
         Otherwise, if the last index is larger than the 
         last index in the list, return all elements from 
         the start index to last element of the list."
    shared actual formal List<Element> span(Integer from,
                                        Integer? to);
	
    "Returns a list containing the elements beginning 
         from the given index, with the given length."
    shared actual formal List<Element> segment(Integer from,
                                           Integer length);*/
    
}
