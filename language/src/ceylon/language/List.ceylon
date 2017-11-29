/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""A collection in which every element has a unique 
   non-negative integer index. The elements of a nonempty 
   list are indexed starting with `0` at the [[first]] 
   element of the list, and ending with the index 
   [[lastIndex]] at the [[last]] element of the list.
   
   - For any nonempty list, `lastIndex==size-1`. 
   - For an empty list, `size==0` and the `lastIndex` is 
     `null`.
   
   Thus, the range of indexes of the list is formed by the 
   expression `0:list.size`.
   
   A `List` is a [[Collection]] of its elements, and a 
   [[Correspondence]] from indexes to elements.
   
   Every list has a well-defined and stable iteration order.
   An [[iterator]] of a nonempty list is required to return 
   the elements of the list in order of increasing index, 
   beginning with the element at index `0`, and ending with
   the element at index `lastIndex`. Thus, every iterator of 
   an immutable list produces exactly the same elements in 
   exactly the same order.
   
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
   
   To iterate the indexes of a `List`, use the following
   idiom:
   
       for (i->char in "hello world".indexed) { ... }
   
   [[Strings|String]], [[sequences|Sequential]], 
   [[tuples|Tuple]], and [[arrays|Array]] are all `List`s,
   and are all of fixed length. Variable-length mutable
   `List`s are also possible."""
see (interface Sequence, 
     interface Empty, 
     class Array)
tagged("Collections")
shared interface List<out Element=Anything>
        satisfies Collection<Element> 
                & Correspondence<Integer,Element> 
                & Ranged<Integer,Element,List<Element>> {
    
    "The first element of this `List`, if any."
    shared actual default Element? first => getFromFirst(0);
    
    "The last element of this `List`, if any."
    shared actual default Element? last => getFromLast(0);
    
    "Returns the element of this list with the given 
     [[index]] if the index refers to an element of this
     list, that is, if `0<=index<=list.lastIndex`, or `null` 
     otherwise. The first element of the list has index `0`,
     and the last element has index [[lastIndex]]."
    shared actual Element? get(Integer index) 
            => getFromFirst(index);
    
    "Returns the element of this list with the given 
     [[index]] if the index refers to an element of this
     list, that is, if `0<=index<=list.lastIndex`, or `null` 
     otherwise. The first element of the list has index `0`, 
     and the last element has index [[lastIndex]]."
    see (function getFromLast)
    shared actual formal Element? getFromFirst(Integer index);
    
    "Returns the element of this list with the given 
     [[index]], where the list is indexed from the _end_ of 
     the list instead of from the start, if the index refers
     to an element of this list, or `null` otherwise. The
     last element of the list has index `0`, and the first
     element has index [[lastIndex]]."
    since("1.1.0")
    shared default Element? getFromLast(Integer index)
            => getFromFirst(size-1-index);
    
    Element getElement(Integer index) {
        if (exists element = getFromFirst(index)) { 
            return element;
        }
        else {
            assert (is Element null);
            return null; 
        }
    }
    
    "The index of the last element of the list, or `null` if 
     the list is empty. Always `size>0 then size-1`."
    see (value List.size)
    shared formal Integer? lastIndex;
    
    "The number of elements in this list, always
     `1 + (lastIndex else -1)`."
    see (value List.lastIndex)
    shared actual default Integer size 
            => 1 + (lastIndex else -1);
    
    "Determines if the given index refers to an element of 
     this list, that is, if `0<=index<=list.lastIndex`."
    shared actual default Boolean defines(Integer index) 
            => 0 <= index < size;
        
    "The rest of the list, without the first element.
     
     This is a lazy operation returning a view of this list."
    shared actual default List<Element> rest 
            => size>1 then Sublist(1,size-1) else [];
    
    shared actual default List<Element> exceptLast 
            => size>1 then Sublist(0, size-2) else [];
    
    "A list containing all indexes of this list.
     
     This is a lazy operation returning a view of this list."
    see (function indexes)
    shared actual default List<Integer> keys => Indexes();
    
    "A list containing the elements of this list in reverse 
     order to the order in which they occur in this list. 
     For every `index` of a reversed `list`:
     
         list.reversed[index]==list[size-1-index]
     
     This is a lazy operation returning a view of this list."
    shared default List<Element> reversed => Reversed();
    
    "A shallow copy of this list, that is, a list with the
     same elements as this list, which do not change if the
     elements of this list change."
    shared actual default List<Element> clone() 
            => sequence();
    
    shared actual default Iterator<Element> iterator() {
        if (size>0) {
            return object
                    satisfies Iterator<Element> {
                variable Integer index = 0;
                value size = outer.size;
                next() => index>=size
                    then finished
                    else getElement(index++);
                string => outer.string + ".iterator()";
            };
        }
        else {
            return emptyIterator;
        }
    }
    
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
        else if (is List<> that) {
            if (this.size != that.size) {
                return false;
            }
            value thisIterator = this.iterator();
            value thatIterator = that.iterator();
            for (_ in 0:size) {
                value thisElement = thisIterator.next();
                value thatElement = thatIterator.next();
                if (exists thisElement) {
                    if (!exists thatElement) {
                        return false;
                    }
                    else if (thisElement != thatElement) {
                        return false;
                    }
                }
                else if (thatElement exists) {
                    return false;
                }
            }
            else {
                return true;
            } 
        }
        else {
            return false;
        }
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
    
    shared actual default
    Boolean shorterThan(Integer length) => size<length;
    
    shared actual default 
    Boolean longerThan(Integer length) => size>length;
    
    "A list containing the elements of this list repeated 
     the [[given number of times|times]], or an empty list
     if `times<=0`. For every `index` of a repeated `list`:
     
         list.repeat(n)[index]==list[index%n]
     
     This is a lazy operation returning a view of this list."
    shared actual default 
    List<Element> repeat(Integer times)
            => switch (times<=>1) 
            case (smaller) []
            case (equal) this
            case (larger) Repeat(times);
    
    shared default actual 
    Element? find(
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
    
    shared default actual 
    Element? findLast(
            Boolean selecting(Element&Object elem)) {
        variable value index = size-1;
        while (index >= 0) {
            if (exists elem = getFromFirst(index--), 
                    selecting(elem)) {
                return elem;
            }
        }
        return null;
    }
    
    "A sublist of this list, starting at the element with
     the given [[index|from]].
     
     This is a lazy operation, returning a view of this list."
    see (function skip, 
         function sublistTo)
    since("1.1.0")
    shared default 
    List<Element> sublistFrom(Integer from) 
            => sublist(from, size-1);
    
    "A sublist of this list, ending at the element with the 
     given [[index|to]].
     
     This is a lazy operation, returning a view of this list."
    see (function take,
        function initial,
        function sublistFrom)
    since("1.1.0")
    shared default 
    List<Element> sublistTo(Integer to) 
            => sublist(0, to);
    
    "A sublist of this list, starting at the element with
     index [[from]], ending at the element with the index 
     [[to]].
     
     This is a lazy operation, returning a view of this list."
    see(function sublistTo, function sublistFrom)
    since("1.1.0")
    shared default 
    List<Element> sublist(Integer from, Integer to)
            => from<=to && from<size && to>=0
            then Sublist {
                from = Integer.largest(0, from); 
                to = Integer.smallest(size-1, to);
            }
            else [];
    
    "Return a list formed by patching the given [[list]] 
     in place of a segment of this list identified by the
     given [[starting index|from]] and [[length]].
     
     This is a lazy operations, returning a view over this 
     list and the given list.
     
     Four special cases are interesting:
     
     - If `length==0`, the patched list has the given values 
       \"inserted\" into this list at the given index `from`.
     - If the given `list` is empty, the patched list has 
       the measure of this list identified by `from:length` 
       \"deleted\".
     - If `from==size`, the patched list is formed by
       appending the given list.
     - If `from==0`, the patched list is formed by 
       prepending the given list.
     
     For example:
     
     - `(-2..2).patch([], 1, 3)` produces the list `{-2, 2}`,
     - `[-2, 2].patch(-1..1, 1)` produces the list 
       `{-2, -1, 0, 1, 2}`, and
     - `0:3.patch(2..0)` produces the list 
       `{0, 1, 2, 2, 1, 0}`.
     
     Finally, to patch a single element, leaving the `size`
     of the list unchanged, explicitly specify `length==1`:
     
     - `[0, 1, 0, 1].patch([-1], 2, 1)` produces the list
       `{0, 1, -1, 1}`.
     
     If `length<0`, or if `from` is outside the range 
     `0..size`, return this list."
    since("1.1.0")
    shared default 
    List<Element|Other> patch<Other>(
        "The list of new elements."
        List<Other> list,
        "The index at which the elements will occur, and
         the start index of the segment to replace."
        Integer from=size,
        "The length of the segment to replace." 
        Integer length=0)
            => length>=0 && 0<=from<=size
            then Patch(list, from, length)
            else this;
    
    "Determine if the given [[list|sublist]] occurs at the 
     start of this list."
    see (function endsWith)
    shared default 
    Boolean startsWith(List<> sublist) 
            => !shorterThan(sublist.size) 
            && everyPair(this, sublist,
                (first, second)
                    => if (exists first, exists second)
                        then first==second
                        else first exists == second exists);
    
    "Determine if the given [[list|sublist]] occurs at the 
     end of this list."
    see (function startsWith)
    shared default 
    Boolean endsWith(List<> sublist)
            => !shorterThan(sublist.size) 
            && everyPair(skip(size-sublist.size), 
                         sublist,
                (first, second)
                    => if (exists first, exists second)
                        then first==second
                        else first exists == second exists);
    
    "The indexes in this list for which the element is not
     null and satisfies the given 
     [[predicate function|selecting]]."
    see (function locations)
    since("1.1.0")
    shared default 
    {Integer*} indexesWhere(
        "The predicate function the indexed elements must 
         satisfy."
        Boolean selecting(Element&Object element)) 
            => { for (index in 0:size) 
                    if (exists element=getFromFirst(index), 
                            selecting(element)) 
                        index };
    
    "The first index in this list for which the element is
     not null and satisfies the given 
     [[predicate function|selecting]]."
    see (function locate)
    since("1.1.0")
    shared default 
    Integer? firstIndexWhere(
        "The predicate function the indexed elements must 
         satisfy."
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
    see (function locateLast)
    since("1.1.0")
    shared default 
    Integer? lastIndexWhere(
        "The predicate function the indexed elements must 
         satisfy."
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
     function|trimming]], along with any null elements, from 
     the start and end of this list, returning a list no 
     longer than this list.
     
     This is an eager operation."
    shared default 
    List<Element> trim(
        "The predicate function that the trimmed elements 
         satisfy."
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
     function|trimming]], along with any null elements, from
     the start of this list, returning a list no longer than 
     this list.
     
     This is an eager operation."
    shared default 
    List<Element> trimLeading(
        "The predicate function that the trimmed elements 
         satisfy."
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
     function|trimming]], along with any null elements, from 
     the end of this list, returning a list no longer than 
     this list.
     
     This is an eager operation."
    shared default 
    List<Element> trimTrailing(
        "The predicate function that the trimmed elements 
         satisfy."
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
    
    "Return two lists, the first containing the elements
     that occur before the given [[index]], the second with
     the elements that occur after the given `index`. If the
     given `index` is outside the range of indexes of this
     list, one of the returned lists will be empty.
     
     For any `list`, and for any integer `index`:
     
         list.slice(index) == [list[...index-1], list[index...]]
     
     This is an eager operation."
    since("1.1.0")
    shared default 
    List<Element>[2] slice(Integer index)
            => [this[...index-1], this[index...]];
    
    "Select the first elements of this list, returning a 
     list no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length. If 
     `length<=0` return an empty list.
     
     For any `list`, and for any integer `length`:
     
         list.initial(length) == list[...length-1] == list[0:length]
     
     This is an eager operation."
    see (function terminal, 
         function sublistTo,
         function take)
    shared default 
    List<Element> initial(Integer length)
            => this[...length-1];
    
    "Select the last elements of the list, returning a list 
     no longer than the given length. If this list is 
     shorter than the given length, return this list. 
     Otherwise return a list of the given length.
     
     For any `list`, and for any integer `length`:
     
         list.terminal(length) == list[size-length...]
     
     This is an eager operation."
    see (function initial)
    shared default 
    List<Element> terminal(Integer length) 
            => this[size-length...];
    
    shared actual default 
    List<Element> span(Integer from, Integer to) {
        if (size>0) {
            value end = size-1;
            if (from <= to) {
                return 
                    if (to >= 0 && from <= end) 
                    then ArraySequence(Array(sublist(from,to)))
                    else [];
            }
            else {
                return 
                    if (from >= 0 && to <= end) 
                    then ArraySequence(Array(sublist(to,from).reversed))
                    else [];
            }
        }
        else {
            return [];
        }
    }
    
    shared actual default 
    List<Element> spanFrom(Integer from)
            => from<size then span(from, size-1) else [];
    
    shared actual default 
    List<Element> spanTo(Integer to) 
            => to>=0 then span(0, to) else [];

    shared actual default
    List<Element> measure(Integer from, Integer length)
            => length > 0 
            then span(from, from+length-1) 
            else [];

    "A sequence containing the results of applying the given 
     mapping to the elements of this list."
    shared default actual 
    [Result+]|[] collect<Result>(
        "The transformation applied to the elements."
        Result collecting(Element element)) {
        if (empty) {
            return [];
        }
        else {
            object list
                    extends Object() 
                    satisfies List<Result> {
                lastIndex => outer.lastIndex;
                size = outer.size;
                getFromFirst(Integer index)
                        => if (0<=index<size) 
                        then collecting(outer.getElement(index))
                        else null;
            }
            return ArraySequence(Array(list));
        }
    }
    
    class Indexes()
            extends Object()
            satisfies List<Integer> {
        
        lastIndex => outer.lastIndex;
        
        getFromFirst(Integer index)
                => defines(index) then index;
        
        clone() => 0:size;
        
        span(Integer from, Integer to)
                => clone()[from..to];
        
        string => if (exists endIndex=lastIndex)
            then "{ 0, ... , ``endIndex`` }"
            else "{}";
        
        iterator() 
                => object satisfies Iterator<Integer> {
                variable value i=0;
                next() => i<size then i++ else finished;
                string => "``outer.string``.iterator()";
            };
        
    }
    
    class Sublist(Integer from, Integer to) 
            extends Object()
            satisfies List<Element> {
        
        assert (from>=0, to>=0, from<=to);
        
        first => outer[from];
        last => outer[to];
        
        size => to-from+1;
        
        lastIndex => to-from;
        
        rest => size == 1 then [] 
                else outer.Sublist(from+1, to);
        
        exceptLast => size == 1 then [] 
                else outer.Sublist(from, to-1);
        
        getFromFirst(Integer index)
                => if (0<=index<=to-from)
                then outer.getFromFirst(index+from)
                else null;
        
        iterator() 
                => let (o = outer)
                object satisfies Iterator<Element> {
                    variable value i = from;
                    next() => if (i <= to)
                        then o.getElement(i++)
                        else finished;
                    string => "``outer.string``.iterator()";
                };
        
        clone() => outer[from..to];
        
        sublist(Integer from, Integer to)
                => outer.sublist {
                    from = Integer.largest(from+this.from,this.from);
                    to = Integer.smallest(to+this.from,this.to);
                };
        
        span(Integer from, Integer to)
                => from <= to 
                then outer.span {
                    from = Integer.largest(from+this.from,this.from);
                    to = Integer.smallest(to+this.from,this.to);
                }
                else outer.span {
                    from = Integer.smallest(from+this.from,this.to);
                    to = Integer.largest(to+this.from,this.from);
                };
        
    }
    
    class Repeat(Integer times)
            extends Object()
            satisfies List<Element> {
        
        assert (times>1);
        
        size => outer.size*times;
        
        lastIndex 
                => let (size = this.size) 
                    (size>0 then size-1);
        
        getFromFirst(Integer index) 
                => let (size = outer.size) 
                    if (index<size*times) 
                    then outer.getFromFirst(index%size)
                    else null;
        
        clone() => outer.clone().Repeat(times);
        
        iterator() => CycledIterator(outer,times);
        
        string => "(``outer.string``).repeat(``times``)";
        
    }
    
    class Patch<Other>(List<Other> list, 
        Integer from, Integer length)
            extends Object()
            satisfies List<Element|Other> {
        
        assert (length>=0);
        assert (0<=from<=outer.size);

        value exactLength => smallest(length, outer.size-from);
        
        size => outer.size+list.size-exactLength;
        
        lastIndex 
                => let (size = this.size) 
                    (size>0 then size-1);
        
        getFromFirst(Integer index) 
                => if (index<from) then
                    outer.getFromFirst(index)
                else if (index-from<list.size) then
                    list.getFromFirst(index-from)
                else
                    outer.getFromFirst(index-list.size+exactLength);
        
        clone() => outer.clone().Patch(list.clone(),from,exactLength);
        
        iterator() 
                => let (iter = outer.iterator(), 
                        patchIter = list.iterator()) 
            object satisfies Iterator<Element|Other> {
                variable value index = -1;
                shared actual Element|Other|Finished next() {
                    if (++index==from) {
                        for (skip in 0:exactLength) {
                            iter.next();
                        }
                    }
                    return if (0<=index-from<list.size)
                        then patchIter.next()
                        else iter.next();
                }
                string => "``outer.string``.iterator()";
            };
        
    }
    
    class Reversed()
            extends Object()
            satisfies List<Element> {
        
        lastIndex => outer.lastIndex;
        size => outer.size;
        first => outer.last;
        last => outer.first;
        
        reversed => outer;
        
        function outerIndex(Integer index) => size-1-index;
        
        getFromFirst(Integer index)
                => if (size>0)
                then outer.getFromFirst(outerIndex(index))
                else null;
        
        span(Integer from, Integer to) 
                => outer[outerIndex(from)..outerIndex(to)];
        
        clone() => outer.clone().reversed;
        
        iterator() 
                => let (outerList = outer) 
            object satisfies Iterator<Element> {
                variable value index = outerIndex(0);
                next() => index<0 
                    then finished 
                    else outerList.getElement(index--);
                string => "``outer.string``.iterator()";
            };
        
    }
    
    "Produces a list with the same indexes as this list. For 
     every index, the element is the result of applying the 
     given [[transformation|List.mapElements.mapping]] 
     function to its associated element in this list. This 
     is a lazy operation, returning a view of this list."
    since("1.3.0")
    shared default 
    List<Result> mapElements<Result>(
        "The function that transforms an index/item pair of
         this list, producing the element of the resulting 
         list."
        Result mapping(Integer index, Element item)) 
            => object
            extends Object()
            satisfies List<Result> {
        
        shared actual Result? getFromFirst(Integer index) {
            if (0 <= index < size) {
                if (exists element 
                        = outer.getFromFirst(index)) {
                    return mapping(index, element);
                }
                else {
                    assert (is Element null);
                    return mapping(index, null);
                }
            }
            else {
                return null;
            }
        }
        
        iterator() 
                => let (it = outer.iterator())
                object satisfies Iterator<Result> {
                    variable value index = 0;
                    next() => if (!is Finished element 
                                    = it.next()) 
                                then mapping(index++, element)
                                else finished;
                };
        
        lastIndex => outer.lastIndex;
        size => outer.size;
        
        clone() => outer.clone().mapElements(mapping);
        
    };
    
}
