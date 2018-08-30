/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"A nonempty, immutable sequence of values. The type 
 `Sequence<Element>` may be abbreviated `[Element+]`.
 
 Given a possibly-empty sequence of type `[Element*]`, the 
 `if (nonempty ...)` construct, or, alternatively, 
 `assert (nonempty ...)`, may be used to narrow to a 
 sequence type to a nonempty sequence type:
 
     [Integer*] nums = ... ;
     if (nonempty nums) {
         Integer first = nums.first;
         Integer max = max(nums);
         [Integer+] squares = nums.collect((Integer i) => i**2));
         [Integer+] sorted = nums.sort(byIncreasing((Integer i) => i));
     }
 
 Operations like `first`, `max()`, `collect()`, and `sort()`, 
 which polymorphically produce a nonempty or non-null output 
 when given a nonempty input are called 
 _emptiness-preserving_.
 
 `Sequence` has the following subtypes:
 
 - [[ArraySequence]], a sequence backed by an [[Array]],
 - [[Range]], an efficient representation of a sequence of 
   adjacent [[enumerable values|Enumerable]],
 - [[Tuple]], a typed linked list, and
 - [[Singleton]], a sequence of just one element."
see (interface Empty, 
       interface Sequential,
       class ArraySequence, 
       class Range, 
       class Tuple, 
       class Singleton)
by ("Gavin")
tagged("Sequences")
shared sealed interface Sequence<out Element=Anything>
        satisfies Element[] 
                & {Element+} {
    
    "The first element of the sequence, that is, the element
     with index `0`."
    shared actual formal Element first;

    "The last element of the sequence, that is, the element
     with index `sequence.lastIndex`."
    shared actual formal Element last;
    
    "Returns `false`, since every `Sequence` contains at
     least one element."
    shared actual Boolean empty => false;
    
    "The non-negative length of this sequence, that is, the
     number of elements in this sequence."
    shared actual formal Integer size;
    
    "The index of the last element of the sequence."
    see (value size)
    shared actual default Integer lastIndex => size-1;
    
    "An integer [[Range]] containing all indexes of this 
     sequence, that is, the range `0..sequence.lastIndex`."
    shared actual default Range<Integer> keys => indexes();
    
    "An integer [[Range]] containing all indexes of this 
     sequence, that is, the range `0..sequence.lastIndex`."
    shared actual default Range<Integer> indexes() 
            => 0..lastIndex;
    
    "This nonempty sequence."
    shared actual default [Element+] sequence() => this;
    
    since("1.3.3")
    shared actual default [Element+] tuple() 
            => arrayToTuple(Array(this));
    
    "The rest of the sequence, without the first element."
    shared actual default Element[] rest 
            => size > 1 
            then Subsequence(1, lastIndex) 
            else [];
    
    "This sequence, without the last element."
    since("1.3.3")
    shared actual default Element[] exceptLast 
            => size > 1 
            then Subsequence(0, lastIndex-1) 
            else [];
    
    since("1.3.3")
    shared actual default 
    Element[] sublist(Integer from, Integer to) 
            => from<=to && from<=lastIndex && to>=0
            then Subsequence {
                from = Integer.largest(0, from);
                to = Integer.smallest(lastIndex, to); 
            }
            else [];
    
    since("1.3.3")
    shared actual default 
    Element[] sublistTo(Integer to) 
            => sublist(0, to);
    
    since("1.3.3")
    shared actual default 
    Element[] sublistFrom(Integer from)
            => sublist(from, size-1);
    
    "A sequence containing the elements of this sequence in
     reverse order to the order in which they occur in this
     sequence."
    shared default actual [Element+] reversed => Reverse();
    
    "Produces a sequence formed by repeating the elements of
     this sequence the given [[number of times|times]], or
     the [[empty sequence|empty]] if `times<=0`."
    shared default actual Element[] repeat(Integer times) 
            => switch (times<=>1) 
            case (smaller) []
            case (equal) this
            case (larger) Repeat(times);
    
    "This nonempty sequence."
    shared actual default [Element+] clone() => this;
    
    /*shared actual default Element[] repeat(Integer times) {
        value resultSize = size*times;
        value array = arrayOfSize(resultSize, first);
        variable value i = 1;
        while (i < resultSize) {
            array[i] = getElement(i%size);
        }
        return ArraySequence(array); 
    }*/
    
    "A nonempty sequence containing the elements of this
     container, sorted according to a function imposing a 
     partial order upon the elements."
    shared default actual 
    [Element+] sort(
        "The function comparing pairs of elements."
        Comparison comparing(Element x, Element y)) {
        value array = Array(this);
        array.sortInPlace(comparing);
        return ArraySequence(array);
    }

    "A nonempty sequence containing the results of applying 
     the given mapping to the elements of this sequence."
    shared default actual 
    [Result+] collect<Result>(
        "The transformation applied to the elements."
        Result collecting(Element element)) {
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
    
    "Return a nonempty sequence containing the given 
     [[element]], followed by the elements of this 
     sequence."
    shared actual default 
    [Other,Element+] withLeading<Other>(Other element)
            => [element, *this];
    
    "Return a nonempty sequence containing the elements of 
     this sequence, followed by the given [[element]]."
    shared actual default
    [Element|Other+] withTrailing<Other>(Other element) 
            => JoinedSequence(this, Singleton(element));
    
    "Return a nonempty sequence containing the elements of 
     this sequence, followed by the given [[elements]]."
    shared actual default 
    [Element|Other+] append<Other>(Other[] elements)
            => if (nonempty elements) 
            then JoinedSequence(this, elements)
            else this;
    
    "Return a nonempty sequence containing the given 
     [[elements]], followed by the elements of this 
     sequence."
    shared actual default 
    [Element|Other+] prepend<Other>(Other[] elements)
            => if (nonempty elements)
            then JoinedSequence(elements, this)
            else this;
    
    shared actual default 
    Boolean contains(Object element) 
            => (super of List<Element>).contains(element);
    
    shared actual default 
    Boolean shorterThan(Integer length) 
            => (super of List<Element>).shorterThan(length);
    
    shared actual default 
    Boolean longerThan(Integer length) 
            => (super of List<Element>).longerThan(length);
    
    shared default actual 
    Element? find(Boolean selecting(Element&Object elem))
            => (super of List<Element>).find(selecting);
    
    shared default actual 
    Element? findLast(Boolean selecting(Element&Object elem))
            => (super of List<Element>).findLast(selecting);
    
    shared actual default 
    Element[][2] slice(Integer index)
            => [this[...index-1], this[index...]];
    
    shared actual default 
    Element[] span(Integer from, Integer to) {
        if (from <= to) {
            return
                if (to < 0 || from > lastIndex)
                    then []
                else if (from <= 0 && to >= lastIndex)
                    then this
                else
                    ArraySequence(Array(sublist(from, to)));
        }
        else {
            return
                if (from < 0 || to > lastIndex)
                    then []
                else if (to <= 0 && from >= lastIndex)
                    then reversed
                else
                    ArraySequence(Array(sublist(to, from).reversed));
        }
    }
    
    shared actual default 
    Element[] spanFrom(Integer from)
            => from<size then span(from, size-1) else [];
    
    shared actual default 
    Element[] spanTo(Integer to)
            => to>=0 then span(0, to) else [];
    
    shared actual default 
    Element[] measure(Integer from, Integer length) 
            => length > 0
            then span(from, from+length-1)
            else [];
    
    shared actual default 
    String string => (super of Sequential<Element>).string;
    
    Element getElement(Integer index) {
        if (exists element = getFromFirst(index)) { 
            return element;
        }
        else {
            assert (is Element null);
            return null; 
        }
    }
    
    class Reverse() 
            extends Object() 
            satisfies [Element+] {
        
        size => outer.size;
        first => outer.last;
        last => outer.first;
        
        reversed => outer;
        
        function outerIndex(Integer index) => size-1-index;
        
        getFromFirst(Integer index) 
                => outer.getFromFirst(outerIndex(index));
        
        span(Integer from, Integer to) 
                => outer[outerIndex(from)..outerIndex(to)];
        
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
    
    class Repeat(Integer times)
            extends Object()
            satisfies [Element+] {
        
        assert (times>1);
        
        last => outer.last;
        first => outer.first;
        size => outer.size*times;
        
        //rest => outer.rest.append(outer.repeat(times-1));
        
        getFromFirst(Integer index)
                => let (size = outer.size)
                    if (index<size*times)
                        then outer.getFromFirst(index%size)
                        else null;
        
        iterator() => CycledIterator(outer,times);
        
    }
    
    class Subsequence(Integer from, Integer to)
            extends Object()
            satisfies [Element+] {
        
        assert (from>=0, to>=0, from<=to);
        
        shared actual Element first {
            if (exists first = outer[from]) {
                return first;
            }
            else {
                assert (is Element null);
                return null;
            }
        }
        shared actual Element last {
            if (exists last = outer[to]) {
                return last;
            }
            else {
                assert (is Element null);
                return null;
            }
        }
        
        size => to-from+1;
        
        rest => size == 1 then [] 
                else outer.Subsequence(from+1, to);
        
        exceptLast => size == 1 then [] 
                else outer.Subsequence(from, to-1);
        
        getFromFirst(Integer index)
                => if (0<=index<=to-from)
                then outer.getFromFirst(index+from)
                else null;
        
        iterator() => outer.take(to+1).skip(from).iterator();
        
        sublist(Integer from, Integer to)
                => outer.sublist {
                    from = Integer.largest(from+this.from,this.from);
                    to = Integer.smallest(to+this.from,this.to);
                };
        
        span(Integer from, Integer to)
                => from<=to 
                then outer.span {
                    from = Integer.largest(from+this.from,this.from);
                    to = Integer.smallest(to+this.from,this.to);
                }
                else outer.span {
                    from = Integer.smallest(from+this.from,this.to);
                    to = Integer.largest(to+this.from,this.from);
                };
        
    }
    
}

"A [[nonempty sequence|Sequence]] of the given [[elements]], 
 or  `null` if the given stream is empty. A non-null, but
 possibly empty, [[sequence|Sequential]] may be obtained 
 using the `else` operator:
 
     [Element*] sequenceOfElements = sequence(elements) else [];"
by ("Gavin")
see (function Iterable.sequence)
tagged("Sequences")
since("1.1.0")
deprecated("Since 1.3.2, [[Iterable.sequence]] has a more
            precise return type, and so this function is no 
            longer useful.")
shared [Element+]|Absent sequence<Element,Absent=Null>
        (Iterable<Element, Absent> elements)
        given Absent satisfies Null {
    if (nonempty sequence = elements.sequence()) {
        return sequence;
    }
    else {
        assert (is Absent null);
        return null;
    }
}

class JoinedSequence<Element>
        ([Element+] firstSeq, [Element+] secondSeq)
        extends Object()
        satisfies [Element+] {
    
    size => firstSeq.size + secondSeq.size;
    
    first => firstSeq.first;
    last => secondSeq.last;
    
    //rest => firstSeq.rest.append(secondSeq);
    
    getFromFirst(Integer index)
            => let (cutover = firstSeq.size) 
                if (index<cutover)
                    then firstSeq.getFromFirst(index) 
                    else secondSeq.getFromFirst(index-cutover);
    
    slice(Integer index) 
            => if (index==firstSeq.size)
            then [firstSeq, secondSeq] 
            else super.slice(index);
    
    spanTo(Integer to) 
            => if (to==firstSeq.size-1) 
            then firstSeq 
            else super.spanTo(to);
    
    spanFrom(Integer from) 
            => if (from==firstSeq.size)
            then secondSeq 
            else super.spanFrom(from);
    
    shared actual 
    Element[] measure(Integer from, Integer length) {
        if (from==0 && 
                length==firstSeq.size) {
            return firstSeq;
        }
        else if (from==firstSeq.size && 
                length>=secondSeq.size) {
            return secondSeq;
        }
        else {
            return super.measure(from, length);
        }
    }
    
    shared actual 
    Element[] span(Integer from, Integer to) {
        if (from<=0 && 
                to==firstSeq.size-1) {
            return firstSeq;
        }
        else if (from==firstSeq.size && 
                to>=size-1) {
            return secondSeq;
        }
        else {
            return super.span(from, to);
        }
    }
    
    iterator() => ChainedIterator(firstSeq,secondSeq);
    
}

