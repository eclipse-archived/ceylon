"""Represents a collection in which every element has a 
   unique non-negative integer index.
   
   A `List` is a [[Collection]] of its elements, and a 
   [[Correspondence]] from indices to elements.
   
   Direct access to a list element by index produces a value 
   of optional type. The following idiom may be used instead 
   of upfront bounds-checking, as long as the list element 
   type is a non-`null` type:
   
       if (exists char = "hello world"[index]) { 
           //do something with char
       }
       else {
           //out of bounds
       }
   
   When an algorithm guarantees that a list contains a given 
   index, the following idiom may be used:
   
       assert (exists char = "hello world"[index]);
       //do something with char
   
   To iterate the indices of a `List`, use the following
   idiom:
   
       for (i->char in "hello world".indexed) { ... }
   
   [[Strings|String]], [[sequences|Sequential]], 
   [[tuples|Tuple]], and [[arrays|Array]] are all `List`s,
   and are all of fixed length. Variable-length mutable
   `List`s are also possible."""
see (`interface Sequence`, 
     `interface Empty`, 
     `class Array`)
shared interface List<out Element>
        satisfies Collection<Element> &
                  Correspondence<Integer,Element> &
                  Ranged<Integer,List<Element>> &
                  Cloneable<List<Element>> {
    
    "The index of the last element of the list, or `null` if 
     the list is empty."
    see (`value List.size`)
    shared formal Integer? lastIndex;
    
    "The number of elements in this sequence, always
     `list.lastIndex+1`."
    see (`value List.lastIndex`)
    shared actual default Integer size 
            => (lastIndex else -1) + 1;
    
    shared actual default Boolean shorterThan(Integer length) 
            => size<length;
    
    shared actual default Boolean longerThan(Integer length) 
            => size>length;
    
    "The rest of the list, without the first element."
    shared actual formal List<Element> rest;
    
    "Determines if the given index refers to an element of 
     this list, that is, if `0<=index<=list.lastIndex`."
    shared actual default Boolean defines(Integer index) 
            => 0 <= index <= (lastIndex else -1);
	
    "Returns the element of this sequence with the given
     index if the index refers to an element of the list,
     that is, if `0<=index<=list.lastIndex`, or `null` 
     otherwise. The first element of the list has index 
     `0`."
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
     same `size` and _entry sets_. The entry set of a list 
     `l` is the set of elements of `l.indexed`. This 
     definition is equivalent to the more intuitive notion 
     that two lists are equal iff they have the same `size` 
     and for every index either:
     
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
        else {
            return null;
        }
    }
    
    "Returns a new `List` that starts with the specified
     element, followed by the elements of this list."
    see (`function following`)
    shared default [Other|Element+] withLeading<Other>(
            "The first element of the resulting sequence."
            Other element)
            => [*(Singleton(element) chain this)];
    
    "Returns a new `List` that contains the specified
     element appended to the end of the elements of this 
     list."
    shared default [Element|Other+] withTrailing<Other>(
            "The last element of the resulting sequence."
            Other element)
            => [*(this chain Singleton(element))];
    
    "Determine if the given list occurs at the start of this 
     list."
    shared default Boolean startsWith(List<Anything> sublist)
            => includesAt(0, sublist);
    
    "Determine if the given list occurs at the end of this 
     list."
    shared default Boolean endsWith(List<Anything> sublist)
            => includesAt(size-sublist.size, sublist);
    
    "Determine if the given list occurs at the given index 
     of this list."
    shared default Boolean includesAt(
            "The index at which this list might occur"
            Integer index, 
            List<Anything> sublist) {
        for (i in 0:sublist.size) {
            value x = this[index+i];
            value y = sublist[i];
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
    
    "Determine if the given list occurs at some index in 
     this list."
    shared default Boolean includes(List<Anything> sublist) {
        for (index in 0:size) {
            if (includesAt(index,sublist)) {
                return true;
            }
        }
        return false;
    }
    
    "The indexes in this list at which the given list 
     occurs."
    shared default {Integer*} inclusions(List<Anything> sublist) 
            => { for (index in 0:size) 
                    if (includesAt(index,sublist)) index };
    
    "The first index in this list at which the given list 
     occurs."
    shared default Integer? firstInclusion(List<Anything> sublist) {
        for (index in 0:size) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "The last index in this list at which the given list 
     occurs."
    shared default Integer? lastInclusion(List<Anything> sublist) {
        for (index in (0:size).reversed) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "Determines if the given value occurs at the given index 
     in this list."
    shared default Boolean occursAt(Integer index, Anything element) {
         value elem = this[index];
         if (exists element) {
             if (exists elem) {
                 return elem==element;
             }
             else {
                 return false;
             }
         }
         else {
             return !elem exists;
         }
    }
    
    "Determines if the given value occurs as an element of 
     this list."
    shared default Boolean occurs(Anything element) {
         for (index in 0:size) {
             if (occursAt(index,element)) {
                 return true;
             }
         }
         return false;
    }
    
    "Determines if this list contains the given value. 
     Equivalent to `occurs()`."
    see (`function occurs`)
    shared actual default Boolean contains(Object element) 
            => occurs(element);
        
    "The indexes in this list at which the given element 
     occurs."
    shared default {Integer*} occurrences(Anything element)
            => { for (index in 0:size) 
                    if (occursAt(index,element)) index };
    
    "The first index in this list at which the given element 
     occurs."
    shared default Integer? firstOccurrence(Anything element) {
        for (index in 0:size) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "The last index in this list at which the given element 
     occurs."
    shared default Integer? lastOccurrence(Anything element) {
        for (index in (0:size).reversed) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
        
    "The indexes in this list for which the element 
     satisfies the given predicate."
    shared default {Integer*} indexes(
            "The predicate the indexed elements must 
             satisfy"
            Boolean selecting(Element element)) 
            => { for (index in 0:size) 
                    //TODO: fix this awful hack
                    if (selecting(this[index] else nothing)) index };
    
    "Trim the elements satisfying the given predicate
     function from the start and end of this list, returning 
     a list no longer than this list."
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
            return this[from..to];
        }
        else {
            return [];
        }
    }
    
    "Trim the elements satisfying the given predicate
     function from the start of this list, returning a list 
     no longer than this list."
    shared default List<Element> trimLeading(Boolean trimming(Element elem)) {
        if (exists l=lastIndex) {
            for (index in 0..l) {
                if (!trimming(this[index] else nothing)) {
                    return this[index..l];
                }
            }
        }
        return [];
    }
    
    "Trim the elements satisfying the given predicate
     function from the end of this list, returning a list no 
     longer than this list."
    shared default List<Element> trimTrailing(Boolean trimming(Element elem)) {
        if (exists l=lastIndex) {
            for (index in l..0) {
                if (!trimming(this[index] else nothing)) {
                    return this[0..index];
                }
            }
        }
        return [];
    }
    
    "Select the first elements of this list, returning a 
     list no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length."
    see (`function List.terminal`)
    shared default List<Element> initial(Integer length)
            => this[0:length];
    
    "Select the last elements of the list, returning a list 
     no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length."
    see (`function List.initial`)
    shared default List<Element> terminal(Integer length) {
        if (exists l = lastIndex, length>0) {
            return this[l-length+1..l];
        }
        else {
            return [];
        }
    }
        
}
