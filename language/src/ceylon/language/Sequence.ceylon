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
see (`interface Empty`, 
	 `class ArraySequence`, 
	 `class Range`, 
	 `class Tuple`, 
	 `class Singleton`)
by ("Gavin")
shared sealed interface Sequence<out Element=Anything>
        satisfies Element[] & 
                  {Element+} {
    
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
    see (`value size`)
    shared actual default Integer lastIndex => size-1;
    
    "A nonempty sequence containing all indexes of this 
     sequence, that is, every index in the range
     `0..sequence.lastIndex`."
    shared actual default [Integer+] keys => 0..lastIndex;
    
    "This nonempty sequence."
    shared default actual [Element+] sequence() => this;
    
    "The rest of the sequence, without the first element."
    shared actual formal Element[] rest;
    
    "A sequence containing the elements of this sequence in
     reverse order to the order in which they occur in this
     sequence."
    shared default actual [Element+] reversed => Reverse();
    
    "Produced a sequence formed by repeating the elements of
     this sequence the given [[number of times|times]], or
     the [[empty sequence|empty]] if `times<=0`."
    shared default actual Element[] repeat(Integer times) 
            => times>0 then Repeat(times) else [];
    
    "This nonempty sequence."
    shared actual default [Element+] clone() => this;
    
    /*shared actual default Element[] repeat(Integer times) {
        value resultSize = size*times;
        value array = arrayOfSize(resultSize, first);
        variable value i = 1;
        while (i < resultSize) {
            array.set(i, getElement(i%size));
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
    [Element[],Element[]] slice(Integer index)
            => [this[...index-1], this[index...]];
    
    shared actual default 
    Element[] measure(Integer from, Integer length) 
            => sublist(from, from+length-1).sequence();
    
    shared actual default 
    Element[] span(Integer from, Integer to) 
            => sublist(from, to).sequence();
    
    shared actual default 
    Element[] spanFrom(Integer from) 
            => sublistFrom(from).sequence();
    
    shared actual default 
    Element[] spanTo(Integer to) 
            => sublistTo(to).sequence();
    
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
        rest => size==1 then [] else outer[size-2..0]; //TODO optimize!
        
        reversed => outer;
        
        getFromFirst(Integer index) 
                => outer.getFromFirst(size-1-index);
        
        measure(Integer from, Integer length) 
                => if (length>0)
                    then let (start = size-1-from)
                        outer[start..start-length+1]
                    else [];
        
        span(Integer from, Integer to) 
                => outer[to..from];
        
        spanFrom(Integer from) 
                => let (endIndex = size-1)
                    if (from<=endIndex)
                        then outer[endIndex-from..0]
                        else [];
        
        spanTo(Integer to)
                => if (to>=0) 
                    then let (endIndex = size-1) 
                        outer[endIndex..endIndex-to]
                    else [];
        
        iterator() 
                => let (outerList = outer) 
            object satisfies Iterator<Element> {
                variable value index = outerList.size-1;
                next() => index<0 
                    then finished 
                    else outerList.getElement(index--);
                string => "``outer.string``.iterator()";
            };
        
    }
    
    class Repeat(Integer times)
            extends Object()
            satisfies [Element+] {
        
        assert (times>0);
        
        last => outer.last;
        
        first => outer.first;
        size => outer.size*times;
        rest => sublistFrom(1).sequence(); //TODO!
        
        getFromFirst(Integer index)
                => let (size = outer.size)
                    if (index<size*times)
                        then outer.getFromFirst(index%size)
                        else null;
        
        iterator() => CycledIterator(outer,times);
        
    }
    
}

"A [[nonempty sequence|Sequence]] of the given [[elements]], 
 or  `null` if the given stream is empty. A non-null, but
 possibly empty, [[sequence|Sequential]] may be obtained 
 using the `else` operator:
 
     [Element*] sequenceOfElements = sequence(elements) else [];"
by ("Gavin")
see (`function Iterable.sequence`)
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
    
    rest => firstSeq.rest.append(secondSeq);
    
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

