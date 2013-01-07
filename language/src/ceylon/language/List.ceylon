doc "Represents a collection in which every element has a 
     unique non-negative integer index.
     
     A `List` is a `Collection` of its elements, and a 
     `Correspondence` from indices to elements.
     
     Direct access to a list element by index produces a
     value of optional type. The following idiom may be
     used instead of upfront bounds-checking, as long as 
     the list element type is a non-`null` type:
     
         value char = \"hello world\"[index];
         if (exists char) { /*do something*/ }
         else { /*out of bounds*/ }
     
     To iterate the indexes of a `List`, use the following
     idiom:
     
         for (i->char in \"hello world\".indexed) { ... }
     
     "
see (Sequence, Empty, Array)
shared interface List<out Element>
        satisfies Collection<Element> &
                  Correspondence<Integer,Element> &
                  Ranged<Integer,List<Element>> &
                  Cloneable<List<Element>> {
    
    doc "The index of the last element of the list, or
         null if the list is empty."
    see (size)
    shared formal Integer? lastIndex;
    
    doc "The number of elements in this sequence, always
         `sequence.lastIndex+1`."
    see (lastIndex)
    shared actual default Integer size => (lastIndex else -1) + 1;
    
    doc "Determines if the given index refers to an element
         of this sequence, that is, if
         `index<=sequence.lastIndex`."
    shared actual default Boolean defines(Integer index) => 
            index <= (lastIndex else -1);
	
    doc "Returns the element of this sequence with the given
         index, or `null` if the given index is past the end
         of the sequence, that is, if
         `index>sequence.lastIndex`. The first element of
         the sequence has index `0`."
    shared actual formal Element? item(Integer index);
    
    shared actual default Iterator<Element> iterator {
        object listIterator
                satisfies Iterator<Element> {
            variable Integer index = 0;
            shared actual Element|Finished next() {
                if (index < (lastIndex else -1)) {
                    if (exists elem = item(index++)) {
                        return elem;
                    }
                    else {
                        throw;
                    }
                }
                else {
                    return finished;
                }
            }
            shared actual String string => "listIterator";
        }
        return listIterator;
    }
    
    doc "Reverse this list, returning a new list."
    shared formal List<Element> reversed;
    
    /*doc "Select the elements between the given indices. If 
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
	
    doc "Returns a list containing the elements beginning 
         from the given index, with the given length."
    shared actual formal List<Element> segment(Integer from,
                                           Integer length);*/
    
    doc "Two `List`s are considered equal iff they have the 
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
                if (exists elem = item(index--)) {
                    if (selecting(elem)) {
                        return elem;
                    }
                }
            }
        }
        return null;
    }
    
    doc "Returns the first element of this `List`, if any."
    shared actual default Element? first => this[0];
    
    doc "Returns the last element of this `List`, if any."
    shared actual default Element? last {
        if (exists i = lastIndex) {
            return this[i];
        }
        return null;
    }
    
    doc "Returns a new `List` that starts with the specified
         element, followed by the elements of this `List`."
    shared default Sequence<Element|Other> withLeading<Other>(
            doc "The first element of the resulting sequence."
            Other element) {
        value sb = SequenceBuilder<Element|Other>();
        sb.append(element);
        if (exists lastIndex) {
            sb.appendAll(this.sequence...);
        }
        if (nonempty seq=sb.sequence) {
            return seq;
        }
        throw; //Can't happen
    }
    
    doc "Returns a new `List` that contains the specified
         element appended to the end of this `List`s'
         elements."
    shared default Sequence<Element|Other> withTrailing<Other>(
            doc "The last element of the resulting sequence."
            Other element) {
        value sb = SequenceBuilder<Element|Other>();
        if (exists lastIndex) {
            sb.appendAll(this.sequence...);
        }
        sb.append(element);
        if (nonempty seq=sb.sequence) {
            return seq;
        }
        throw; //Can't happen
    }

}
