"""Represents a collection in which every element has a 
   unique non-negative integer index. The elements of a
   nonempty list are indexed starting with `0` at the 
   [[first]] element of the list, and ending with the index
   [[lastIndex]] at the [[last]] element of the list.
   
   - For any nonempty list, `lastIndex==size-1`. 
   - For an empty list, `size==0` and the `lastIndex` is 
     `null`.
   
   Thus, the range of indexes of the list is formed by the 
   expression `0:list.size`.
   
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
                  Ranged<Integer,Element,List<Element>> {
    
    "The index of the last element of the list, or `null` if 
     the list is empty."
    see (`value List.size`)
    shared formal Integer? lastIndex;
    
    "The number of elements in this list, always
     `list.lastIndex+1`."
    see (`value List.lastIndex`)
    shared actual default Integer size 
            => (lastIndex else -1) + 1;
    
    shared actual default Boolean shorterThan(Integer length) 
            => size<length;
    
    shared actual default Boolean longerThan(Integer length) 
            => size>length;
    
    "The rest of the list, without the first element. This 
     is a lazy operation."
    shared actual default List<Element> rest => Rest(1);
    
    "A sublist of this list, starting at the element with
     the given [[index|from]]. This is a lazy operation, 
     returning a view of this list."
    see (`function skip`)
    shared default List<Element> sublistFrom(Integer from) 
            => from<0 then this else Rest(from); 
    
    "A sublist of this list, ending at the element with the 
     given [[index|to]]. This is a lazy operation, returning 
     a view of this list."
    see (`function take`)
    shared default List<Element> sublistTo(Integer to) 
            => to<0 then this else Sublist(to);
    
    "A sublist of this list, starting at the element with
     index [[from]], ending at the element with the index 
     [[to]]. This is a lazy operation, returning a view of 
     this list."
    shared default List<Element> sublist(Integer from, Integer to) 
            => sublistTo(to).sublistFrom(from);
    
    "Determines if the given index refers to an element of 
     this list, that is, if `0<=index<=list.lastIndex`."
    shared actual default Boolean defines(Integer index) 
            => 0 <= index < size;
    
    "Returns the element of this list with the given 
     [[index]] if the index refers to an element of this
     list, that is, if `0<=index<=list.lastIndex`, or 
     `null` otherwise. The first element of the list has 
     index `0`, and the last element has index [[lastIndex]]."
    see (`function getFromLast`)
    shared formal Element? getFromFirst(Integer index);
    
    "Returns the element of this list with the given 
     [[index]], where the list is indexed from the _end_ of 
     the list instead of from the start, if the index refers
     to an element of this list, or `null` otherwise. The
     last element of the list has index `0`, and the first
     element has index [[lastIndex]]."
    shared default Element? getFromLast(Integer index)
            => getFromFirst(size-1-index);
    
    "Returns the element of this list with the given 
     [[index]] if the index refers to an element of this
     list, that is, if `0<=index<=list.lastIndex`, or `null` 
     otherwise. The first element of the list has index `0`,
     and the last element has index [[lastIndex]]."
    shared actual default Element? get(Integer index) 
            => getFromFirst(index);
    
    shared actual default Iterator<Element> iterator() {
        if (size>0) {
            object listIterator
                    satisfies Iterator<Element> {
                variable Integer index = 0;
                value size = outer.size;
                next() => index>=size then finished 
                                      else getElement(index++);
                string => "(``outer.string``).iterator()";
            }
            return listIterator;
        }
        else {
            return emptyIterator;
        }
    }
    
    "A list containing the elements of this list in reverse 
     order. This is a lazy operation returning a view of 
     this list."
    see (`function reverse`)
    shared default List<Element> reversed => Reversed();
    
    see (`value reversed`)
    shared actual default Element[] reverse() => [*reversed];
    
    "Two `List`s are considered equal iff they have the 
     same `size` and _entry sets_. The entry set of a list 
     `list` is the set of elements of `list.indexed`. This 
     definition is equivalent to the more intuitive notion 
     that two lists are equal iff they have the same `size` 
     and for every index either:
     
     - the lists both have the element `null`, or
     - the lists both have a non-null element, and the
       two elements are equal.
     
     As a special exception, a [[String]] is not equal to 
     any list which is not also a [[String]]."
    shared actual default Boolean equals(Object that) {
        if (is String that) {
            return false;
        }
        if (is List<Anything> that) {
            if (that.size==size) {
                for (index in 0:size) {
                    value x = getFromFirst(index);
                    value y = that.getFromFirst(index);
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
    
    shared default actual Element? find(
        Boolean selecting(Element&Object elem)) {
        variable value index = 0;
        while (index<size) {
            if (exists elem = getFromFirst(index++)) {
                if (selecting(elem)) {
                    return elem;
                }
            }
        }
        return null;
    }
    
    shared default actual Element? findLast(
            Boolean selecting(Element&Object elem)) {
        variable value index = size-1;
        while (index >= 0) {
            if (exists elem = getFromFirst(index--)) {
                if (selecting(elem)) {
                    return elem;
                }
            }
        }
        return null;
    }
    
    "Returns the first element of this `List`, if any."
    shared actual default Element? first => getFromFirst(0);
    
    "Returns the last element of this `List`, if any."
    shared actual default Element? last => getFromLast(0);
    
    "A list containing all indexes of this list. This is a 
     lazy operation."
    shared actual default List<Integer> keys => Indexes();
    
    "Returns a new `List` that starts with the specified
     [[element]], followed by the elements of this list,
     in the order they occur in this list."
    see (`function follow`)
    shared default [Other,Element*] withLeading<Other>(
            "The first element of the resulting sequence."
            Other element)
            => [element, *this];
    
    "Returns a new `List` that starts with the elements of 
     this list, in the order they occur in this list, and 
     ends with the specified [[element]]."
    shared default [Element|Other+] withTrailing<Other>(
            "The last element of the resulting sequence."
            Other element)
            => [*(this chain Singleton(element))];
    
    "Return a sequence containing the elements of this list, 
     in the order in which they occur in this list, followed 
     by the given [[elements]], in the order in which they 
     occur in the given stream."
    see (`function extend`, 
         `function chain`,
         `function concatenate`)
    shared default [Element|Other*] append<Other>({Other*} elements) 
            => [*(this chain elements)];
    
    "Return a sequence containing the given [[elements]], in 
     the order in which they occur in the given stream,
     followed by the elements of this list, in the order in 
     which they occur in this list."
    shared default [Element|Other*] prepend<Other>({Other*} elements) 
            => [*(elements chain this)];
    
    "Return a list formed by extending this list with the 
     elements of the given [[list]]. This is a lazy operation
     returning a view over this list and the given list."
    see (`function append`,
         `function chain`)
    shared default List<Element|Other> extend<Other>(List<Other> list) 
            => Extend(list);
    
    "Return a list formed by patching the given [[list]] 
     in place of a segment of this list identified by the
     given [[starting index|from]] and [[length]]. This is a
     lazy operations, returning a view over this list and 
     the given list. Two special cases are interesting:
     
     - If `length=0`, the patched list has the given values 
       \"inserted\" into this list at the given index `from`.
     - If the given `list` is empty, the patched last has 
       the segment of this list identified by `from:length` 
       \"deleted\".
     
     For example:
     
     - `(-2..2).patch([],1,3)` produces the list `{-2,2}`, 
       and
     - `[-2, 2].patch(-1..1,1)` produces the list 
       `{-2,-1,0,1,2}`.'
     
     If `length<0`, return this list."
    shared default List<Element|Other> patch<Other>(
        "The list of new elements."
        List<Other> list,
        "The index at which the elements will occur, and
         the start index of the segment to replace."
        Integer from,
        "The length of the segment to replace." 
        Integer length=0)
            => length>=0 && 0<=from<size 
                    then Patch(list, from, length)
                    else this;
    
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
            "The index at which the [[sublist]] might occur."
            Integer index, 
            List<Anything> sublist) {
        for (i in 0:sublist.size) {
            value x = getFromFirst(index+i);
            value y = sublist.getFromFirst(i);
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
        if (sublist.empty) {
            return true;
        }
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
    shared default Boolean occursAt(
            "The index at which the value might occur."
            Integer index, 
            Anything element) {
        value elem = getFromFirst(index);
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
     Returns `true` for every element of this list."
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
        
    "The indexes in this list for which the element is not
     null and satisfies the given 
     [[predicate function|selecting]]."
    shared default {Integer*} indexesWhere(
            "The predicate function the indexed elements 
             must satisfy"
            Boolean selecting(Element&Object element)) 
            => { for (index in 0:size) 
                    if (exists element=getFromFirst(index), 
                            selecting(element)) 
                        index };
    
    "The first index in this list for which the element is
     not null and satisfies the given 
     [[predicate function|selecting]]."
    shared default Integer? firstIndexWhere(
            "The predicate function the indexed elements 
             must satisfy"
            Boolean selecting(Element&Object element)) {
        variable value index = 0;
        while (index<size) {
            if (exists element=getFromFirst(index), 
                selecting(element)) {
                return index;
            }
            index++;
        }
        return null;
    }
    
    "The last index in this list for which the element is
     not null and satisfies the given 
     [[predicate function|selecting]]."
    shared default Integer? lastIndexWhere(
            "The predicate function the indexed elements 
             must satisfy."
            Boolean selecting(Element&Object element)) {
        variable value index = size;
        while (index>0) {
            index--;
            if (exists element=getFromFirst(index), 
                selecting(element)) {
                return index;
            }
        }
        return null;
    }
    
    "Trim the elements satisfying the given [[predicate 
     function|trimming]] from the start and end of this list, 
     returning a list no longer than this list.
     
     This is an eager operation."
    shared default List<Element> trim(
            "The predicate function that the trimmed 
             elements satisfy."
            Boolean trimming(Element&Object elem)) {
        if (size>0) {
            value end = size-1;
            variable Integer from=-1;
            variable Integer to=-1;
            for (index in 0..end) {
                if (exists elem=getFromFirst(index),
                    !trimming(elem)) {
                    from = index;
                    break;
                }
            }
            else {
                return [];
            }
            for (index in end..0) {
                if (exists elem=getFromFirst(index),
                    !trimming(elem)) {
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
    
    "Trim the elements satisfying the given [[predicate 
     function|trimming]] from the start of this list, 
     returning a list no longer than this list.
     
     This is an eager operation."
    shared default List<Element> trimLeading(
            "The predicate function that the trimmed 
             elements satisfy."
            Boolean trimming(Element&Object elem)) {
        if (size>0) {
            value end = size-1;
            for (index in 0..end) {
                if (exists elem=getFromFirst(index),
                    !trimming(elem)) {
                    return this[index..end];
                }
            }
        }
        return [];
    }
    
    "Trim the elements satisfying the given [[predicate 
     function|trimming]] from the end of this list, 
     returning a list no longer than this list.
     
     This is an eager operation."
    shared default List<Element> trimTrailing(
            "The predicate function that the trimmed 
             elements satisfy."
            Boolean trimming(Element&Object elem)) {
        if (size>0) {
            value end = size-1;
            for (index in end..0) {
                if (exists elem=getFromFirst(index),
                    !trimming(elem)) {
                    return this[0..index];
                }
            }
        }
        return [];
    }
    
    "Select the first elements of this list, returning a 
     list no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length.
     
     This is an eager operation."
    see (`function List.terminal`)
    shared default List<Element> initial(Integer length)
            => this[0:length];
    
    "Select the last elements of the list, returning a list 
     no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length.
     
     This is an eager operation."
    see (`function List.initial`)
    shared default List<Element> terminal(Integer length) {
        if (length>=size) {
            return this;
        }
        else if (size>0, length>0) {
            return this[size-length..size-1];
        }
        else {
            return [];
        }
    }
    
    Element getElement(Integer index) {
        value element = getFromFirst(index);
        if (exists element) { 
            return element;
        }
        else {
            assert (is Element null);
            return null; 
        }
    }
    
    shared actual default List<Element> span
                            (Integer from, Integer to) {
        if (size>0) {
            value end = size-1;
            if (from <= to) {
                if (to < 0 || from > end) {
                    return [];
                }
                return ArraySequence(populateArray(to-from+1, 
                    (Integer i) => getElement(from+i)));
            }
            else {
                if (from < 0 || to > end) {
                    return [];
                }
                return ArraySequence(populateArray(from-to+1,
                    (Integer i) => getElement(from-i)));
            }
        }
        else {
            return [];
        }
    }
    
    shared actual default List<Element> spanFrom(Integer from) {
        if (from<=0) {
            return clone();
        }
        else if (from<size) {
            return ArraySequence(populateArray(size-from, 
                (Integer i) => getElement(from+i)));
        }
        else {
            return [];
        }
    }
    
    shared actual default List<Element> spanTo(Integer to) {
        if (to>=size-1) {
            return this;
        }
        else if (to>=0) {
            return ArraySequence(populateArray(to+1, 
                    (Integer i) => getElement(i)));
        }
        else {
            return [];
        }
    }
    
    shared actual default List<Element> segment
                            (Integer from, Integer length) {
        if (length>size) {
            return this;
        }
        else if (length>=1) {
            return ArraySequence(populateArray(length, 
                    (Integer i) => getElement(from+i)));
        }
        else {
            return [];
        }
    }
    
    "Return two lists, the first containing the elements
     that occur before the given [[index]], the second with
     the elements that occur after the given `index`. If the
     given `index` is outside the range of indices of this
     list, one of the returned lists will be empty.
     
     This is an eager operation."
    shared default [List<Element>,List<Element>] slice
                (Integer index)
            => [this[...index-1], this[index...]];
    
    shared actual default Element[] sequence() {
        if (empty) {
            return [];
        }
        else {
            return populateSequence(size, getElement);
        }
    }
    
    shared actual default List<Element> clone() 
            => Array(this);
    
    
    class Indexes()
            extends Object()
            satisfies List<Integer> {
        
        lastIndex => outer.lastIndex;
        
        getFromFirst(Integer index)
                => defines(index) then index;
        
        clone() => 0:size;
        
        segment(Integer from, Integer length)
                => clone()[from:length];
        
        span(Integer from, Integer to)
                => clone()[from..to];
        spanFrom(Integer from) => clone()[from...];
        spanTo(Integer to) => clone()[...to];
        
        shared actual String string {
            if (exists endIndex=lastIndex) {
                return "{ 0, ... , ``endIndex`` }";
            }
            else {
                return "{}";
            }
        }
        
        shared actual Iterator<Integer> iterator() {
            object iterator satisfies Iterator<Integer> {
                variable value i=0;
                next() => i<size then i++ else finished;
                string => "``outer.string``.iterator()";
            }
            return iterator;
        }
        
    }
    
    class Rest(Integer from)
            extends Object()
            satisfies List<Element> {
        
        assert (from>=0);
        
        shared actual Element? getFromFirst(Integer index) {
            if (index<0) {
                return null;
            }
            else {
                return outer.getFromFirst(index+from);
            }
        }
        
        shared actual Integer? lastIndex {
            value size = outer.size-from;
            return size>0 then size-1;
        }
        
        segment(Integer from, Integer length) 
                => outer[from+this.from:length];
        
        span(Integer from, Integer to) 
                => outer[from+this.from..to+this.from];
        spanFrom(Integer from) => outer[from+this.from...];
        spanTo(Integer to) => outer[this.from..to+this.from];
        
        clone() => outer.clone().Rest(from);
        
        shared actual Iterator<Element> iterator() {
            value iter = outer.iterator();
            variable value i=0;
            while (i++<from) {
                iter.next();
            }
            object iterator satisfies Iterator<Element> {
                next() => iter.next();
                string => "``outer.string``.iterator()";
            }
            return iterator;
        }
        
    }
    
    class Sublist(Integer to)
            extends Object()
            satisfies List<Element> {
        
        assert (to>=0);
        
        shared actual Element? getFromFirst(Integer index) {
            if (index>=0 && index<=to) {
                return outer.getFromFirst(index);
            }
            else {
                return null;
            }
        }
        
        shared actual Integer? lastIndex {
            value endIndex = outer.size-1;
            if (endIndex>=0) {
                return endIndex<to then endIndex else to;
            }
            else {
                return null;
            }
        }
        
        segment(Integer from, Integer length) 
                => from+length-1>to 
                    then outer[from:to] 
                    else outer[from:length];
        
        span(Integer from, Integer to) 
                => to>this.to
                    then outer[from..this.to] 
                    else outer[from..to];
        spanFrom(Integer from) => outer[from..to];
        spanTo(Integer to)
                => to>this.to
                    then outer[...this.to] 
                    else outer[...to];
        
        clone() => outer.clone().Sublist(to);
        
        shared actual Iterator<Element> iterator() {
            value iter = outer.iterator();
            variable value i=0;
            object iterator satisfies Iterator<Element> {
                next() => i++>to then finished else iter.next();
                string => "``outer.string``.iterator()";
            }
            return iterator;
        }
        
    }
    
    class Extend<Other>(List<Other> list)
            extends Object()
            satisfies List<Element|Other> {
        
        size => outer.size+list.size;
        
        shared actual Integer? lastIndex {
            value size = this.size;
            return size>0 then size-1;
        }
        
        shared actual <Element|Other>? getFromFirst(Integer index) {
            value size = outer.size;
            if (index<size) {
                return outer.getFromFirst(index);
            }
            else {
                return list.getFromFirst(index-size);
            }
        }
        
        clone() => outer.clone().Extend(list.clone());
        
        iterator() => ChainedIterator(outer,list);
        
    }
    
    class Patch<Other>(List<Other> list, 
        Integer from, Integer length)
            extends Object()
            satisfies List<Element|Other> {
        
        assert (length>=0);
        assert (0<=from<outer.size);
        
        size => outer.size+list.size-length;
        
        shared actual Integer? lastIndex {
            value size = this.size;
            return size>0 then size-1;
        }
        
        shared actual <Element|Other>? getFromFirst(Integer index) {
            if (index<from) {
                return outer.getFromFirst(index);
            }
            else if (index-from<list.size) {
                return list.getFromFirst(index-from);
            }
            else {
                return outer.getFromFirst(index-list.size+length);
            }
        }
        
        clone() => outer.clone().Patch(list.clone(),from,length);
        
        shared actual Iterator<Element|Other> iterator() {
            value iter = outer.iterator();
            value patchIter = list.iterator();
            object iterator satisfies Iterator<Element|Other> {
                variable value index = -1;
                shared actual Element|Other|Finished next() {
                    if (++index==from) {
                        for (skip in 0:length) {
                            iter.next();
                        }
                    }
                    if (0<=index-from<list.size) {
                        return patchIter.next();
                    }
                    else {
                        return iter.next();
                    }
                }
                string => "``outer.string``.iterator()";
            }
            return iterator;
        }
        
    }
    
    class Reversed()
            extends Object()
            satisfies List<Element> {
        
        lastIndex => outer.lastIndex;
        
        shared actual Element? getFromFirst(Integer index) {
            if (size>0) {
                return outer.getFromFirst(size-1-index);
            }
            else {
                return null;
            }
        }
        
        shared actual List<Element> segment(Integer from, Integer length) {
            if (size>0, length>1) {
                value start = size-1-from;
                return outer[start..start-length+1];
            }
            else {
                return [];
            }
        }
        
        span(Integer from, Integer to) => outer[to..from];
        
        shared actual List<Element> spanFrom(Integer from) {
            value endIndex = size-1;
            if (endIndex>=0, from<=endIndex) { 
                return outer[endIndex-from..0];
            }
            else {
                return [];
            }
        }
        
        shared actual List<Element> spanTo(Integer to) {
            value endIndex = size-1;
            if (endIndex>=0, to>=0) { 
                return outer[endIndex..endIndex-to];
            }
            else {
                return [];
            }
        }
        
        clone() => outer.clone().reversed;
        
        shared actual Iterator<Element> iterator() {
            value outerList=outer;
            object iterator satisfies Iterator<Element> {
                variable value index=outerList.size-1;
                next() => index<0 then finished 
                                  else outerList.getElement(index--);
                string => "``outer.string``.iterator()";
            }
            return iterator;
        }
        
    }
    
}
