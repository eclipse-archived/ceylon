doc "Abstract supertype of containers whose elements may be 
     iterated. An iterable container need not be finite, but
     its elements must at least be countable. There may not
     be a well-defined iteration order, and so the order of
     iterated elements may not be stable.
     
     An instance of `Iterable` may be iterated using a `for`
     loop:
     
         for (c in \"hello world\") { ... }
     
     `Iterable` and its subtypes define various operations
     that return other iterable objects. Such operations 
     come in two flavors:
     
     - _Lazy_ operations return a \"view\" of the receiving
       iterable object. If the underlying iterable object is
       mutable, then changes to the underlying object will
       be reflected in the resulting view. Lazy operations
       are usually efficient, avoiding memory allocation or
       iteration of the receiving iterable object.
       
     - _Eager_ operations return an immutable object. If the
       receiving iterable object is mutable, changes to this
       object will not be reflected in the resulting 
       immutable object. Eager operations are often 
       expensive, involving memory allocation and iteration
       of the receiving iterable object.
     
     Lazy operations are preferred, because they can be 
     efficiently chained. For example:
     
         string.filter((Character c) => c.letter).map((Character c) => c.uppercased)
     
     is much less expensive than:
     
         string.select((Character c) => c.letter).collect((Character c) => c.uppercased)
     
     Furthermore, it is always easy to produce a new 
     immutable iterable object given the view produced by a
     lazy operation. For example:
     
         [ string.filter((Character c) => c.letter).map((Character c) => c.uppercased)... ]
     
     Lazy operations normally return an instance of 
     `Iterable` or `Map`.
     
     However, there are certain scenarios where an eager 
     operation is more useful, more convenient, or no more 
     expensive than a lazy operation, including:
     
     - sorting operations, which are eager by nature,
     - operations which preserve emptiness/nonemptiness of
       the receiving iterable object.
     
     Eager operations normally return a sequence."
see (Collection)
by "Gavin"
shared native interface Iterable<out Element, out Absent=Null>
        satisfies Container<Element,Absent> 
        given Absent satisfies Null {
    
    doc "An iterator for the elements belonging to this 
         container."
    shared formal Iterator<Element> iterator;
    
    doc "Determines if the iterable object is empty, that is
         to say, if the iterator returns no elements."
    shared actual default Boolean empty =>
            iterator.next() is Finished;
    
    shared default Integer size => count((Element e) => true);
    
    shared actual default Boolean contains(Object element) => 
            any(ifExists(element.equals));
    
    doc "The first element returned by the iterator, if any.
         This should always produce the same value as
         `iterable.iterator.head`."
    shared actual default Absent|Element first {
        Element? first;
        if (!is Finished next = iterator.next()) {
            first = next;
        }
        else {
            first = null;
        }
        assert (is Absent|Element first);
        return first;
    }
    
    doc "The last element returned by the iterator, if any.
         Iterables are potentially infinite, so calling this
         might never return; also, this implementation will
         iterate through all the elements, which might be
         very time-consuming."
    shared actual default Absent|Element last {
        variable Absent|Element e = first;
        for (x in this) {
            e = x;
        }
        return e;
    }
    
    doc "Returns an iterable object containing all but the 
         first element of this container."
    shared default {Element*} rest => skipping(1);
    
    doc "A sequence containing the elements returned by the
         iterator."
    shared default Element[] sequence => [ for (x in this) x ];
    
    doc "An `Iterable` containing the results of applying
         the given mapping to the elements of to this 
         container."
    see (collect)
    shared default {Result*} map<Result>(
            doc "The mapping to apply to the elements."
            Result collecting(Element elem)) =>
                    { for (elem in this) collecting(elem) };
    
    doc "An `Iterable` containing the elements of this 
         container that satisfy the given predicate."
    see (select)
    shared default {Element*} filter(
            doc "The predicate the elements must satisfy."
            Boolean selecting(Element elem)) =>
                    { for (elem in this) if (selecting(elem)) elem };
    
    doc "The result of applying the accumulating function to 
         each element of this container in turn." 
    shared default Result fold<Result>(Result initial,
            doc "The accumulating function that accepts an
                 intermediate result, and the next element."
            Result accumulating(Result partial, Element elem)) {
        variable value r = initial;
        for (e in this) {
            r = accumulating(r, e);
        }
        return r;
    }
    
    doc "The first element which satisfies the given 
         predicate, if any, or `null` otherwise."
    shared default Element? find(
            doc "The predicate the element must satisfy."
            Boolean selecting(Element elem)) {
        for (e in this) {
            if (selecting(e)) {
                return e;
            }
        }
        return null;
    }
    
    doc "The last element which satisfies the given
         predicate, if any, or `null` otherwise."
    shared default Element? findLast(
            doc "The predicate the element must satisfy."
            Boolean selecting(Element elem)) {
        variable Element? last = null;
        for (e in this) {
            if (selecting(e)) {
                last = e;
            }
        }
        return last;
    }
    
    doc "A sequence containing the elements of this
         container, sorted according to a function 
         imposing a partial order upon the elements.
         
         For convenience, the functions `byIncreasing()` 
         and `byDecreasing()` produce a suitable 
         comparison function:
         
             \"Hello World!\".sort(byIncreasing((Character c) c.lowercased))
         
         This operation is eager by nature."
    see (byIncreasing, byDecreasing)
    shared native default Element[] sort(
            doc "The function comparing pairs of elements."
            Comparison? comparing(Element x, Element y));
    
    doc "A sequence containing the results of applying the
         given mapping to the elements of this container. An 
         eager counterpart to `map()`."
    see (map)
    shared default Result[] collect<Result>(
            doc "The transformation applied to the elements."
            Result collecting(Element element)) =>
                    map(collecting).sequence;
    
    doc "A sequence containing the elements of this 
         container that satisfy the given predicate. An 
         eager counterpart to `filter()`."
    see (filter)
    shared default Element[] select(
            doc "The predicate the elements must satisfy."
            Boolean selecting(Element element)) =>
                    filter(selecting).sequence;
    
    doc "Return `true` if at least one element satisfies the
         predicate function."
    shared default Boolean any(
            doc "The predicate that at least one element 
                 must satisfy."
            Boolean selecting(Element e)) {
        for (e in this) {
            if (selecting(e)) {
                return true;
            }
        }
        return false;
    }
    
    doc "Return `true` if all elements satisfy the predicate
         function."
    shared default Boolean every(
            doc "The predicate that all elements must 
                 satisfy."
            Boolean selecting(Element e)) {
        for (e in this) {
            if (!selecting(e)) {
                return false;
            }
        }
        return true;
    }
    
    doc "Produce an `Iterable` containing the elements of
         this iterable object, after skipping the first 
         `skip` elements. If this iterable object does not 
         contain more elements than the specified number of 
         elements, the `Iterable` contains no elements."
    shared default {Element*} skipping(Integer skip) {
        if (skip <= 0) { 
            return this;
        }
        else {
            object iterable satisfies {Element*} {
                shared actual Iterator<Element> iterator {
                    value iterator = outer.iterator;
                    variable value i=0;
                    while (i++<skip && !iterator.next() is Finished) {}
                    return iterator;
                }
                shared actual Element? first {
                    if (!is Finished first = iterator.next()) {
                        return first;
                    }
                    else {
                        return null;
                    }
                }
                shared actual Element? last => outer.last;
            }
            return iterable;
        }
    }
    
    doc "Produce an `Iterable` containing the first `take`
         elements of this iterable object. If the specified 
         number of elements is larger than the number of 
         elements of this iterable object, the `Iterable` 
         contains the same elements as this iterable object."
    shared default {Element*} taking(Integer take) {
        if (take <= 0) { 
            return {}; 
        }
        else {
            object iterable satisfies {Element*} {
                shared actual Iterator<Element> iterator {
                    value outerIterable { return outer; }
                    object iterator satisfies Iterator<Element> {
                        value iter = outerIterable.iterator;
                        variable value i=0;
                        actual shared Element|Finished next() {
                            return ++i>take then finished 
                                    else iter.next();
                        }
                    }
                    return iterator;
                }
                shared actual Element? first => outer.first;
                shared actual Element? last => outer.last; //TODO!!!!
            }
            return iterable;
        }
    }
    
    doc "Produce an `Iterable` containing every `step`th 
         element of this iterable object. If the step size 
         is `1`, the `Iterable` contains the same elements 
         as this iterable object. The step size must be 
         greater than zero. The expression
         
             (0..10).by(3)
         
         results in an iterable object with the elements
         `0`, `3`, `6`, and `9` in that order."
    throws (Exception, "if the given step size is nonpositive, 
                        i.e. `step<1`") //TODO: better exception type
    shared default {Element*} by(Integer step) {
        if (step <= 0) {
            throw Exception("step size must be greater than zero");
        }
        else if (step == 1) {
            return this;
        } 
        else {
            object iterable satisfies {Element*} {
                shared actual Iterator<Element> iterator {
                    value outerIterable { return outer; }
                    object iterator satisfies Iterator<Element> {
                        value iter = outerIterable.iterator;
                        actual shared Element|Finished next() {
                            value next = iter.next();
                            variable value i=0;
                            while (++i<step && !iterator.next() is Finished) {}
                            return next;
                        }
                    }
                    return iterator;
                }
            }
            return iterable;
        }
    }
    
    doc "Return the number of elements in this `Iterable` 
         that satisfy the predicate function."
    shared default Integer count(
            doc "The predicate satisfied by the elements to
                 be counted."
            Boolean selecting(Element element)) {
        variable value count=0;
        for (elem in this) {
            if (exists elem) {
                if (selecting(elem)) {
                    count++;
                }
            }
        }
        return count;
    }
    
    doc "The non-null elements of this `Iterable`, in their
         original order. For null elements of the original 
         `Iterable`, there is no entry in the resulting 
         iterable object."
    shared default {<Element&Object>*} coalesced =>
            { for (e in this) if (exists e) e };
    
    doc "All entries of form `index->element` where `index` 
         is the position at which `element` occurs, for every
         non-null element of this `Iterable`, ordered by
         increasing `index`. For a null element at a given
         position in the original `Iterable`, there is no 
         entry with the corresponding index in the resulting 
         iterable object. The expression 
         
             { \"hello\", null, \"world\" }.indexed
             
         results in an iterable object with the entries
         `0->\"hello\"` and `2->\"world\"`."
    shared default {<Integer->Element&Object>*} indexed {
            object iterable satisfies {<Integer->Element&Object>*} {
                shared actual Iterator<Integer->Element&Object> iterator {
                    value outerIterable { return outer; }
                    object iterator satisfies Iterator<Integer->Element&Object> {
                        value iter = outerIterable.iterator;
                        variable value i=0;
                        actual shared <Integer->Element&Object>|Finished next() {
                            value next = iter.next();
                            if (!is Finished next) {
                                if (exists next) {
                                    return i++->next;
                                }
                                else {
                                    i++;
                                    return this.next();
                                }
                            }
                            else {
                                return finished;
                            }
                        }
                    }
                    return iterator;
                }
            }
            return iterable.coalesced;
    }
    
    doc "The elements of this iterable object, in their
         original order, followed by the elements of the 
         given iterable object also in their original
         order."
    shared default {<Element|Other>*} chain<Other>(
            {Other*} other) {
        object chained satisfies {<Element|Other>*} {
            shared actual Iterator<Element|Other> iterator {
                return ChainedIterator(outer, other);
            }
        }
        return chained;
    }
    
    doc "Creates a Map that contains this `Iterable`'s
         elements, grouped in `Sequence`s under the
         keys provided by the grouping function."
    shared default native Map<Grouping,[Element+]> group<Grouping>(
                doc "A function that must return the key under
                     which to group the specified element."
                Grouping grouping(Element elem))
            given Grouping satisfies Object;
    
}

Boolean ifExists(Boolean predicate(Object val))(Anything val) {
    if (exists val) {
        return predicate(val);
    }
    else {
        return false;
    }
}
