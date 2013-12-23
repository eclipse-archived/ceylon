import ceylon.language { internalFirst = first }

"""Abstract supertype of categories whose elements may be 
   iterated. An iterable category need not be finite, but
   its elements must at least be countable. There may not
   be a well-defined iteration order, and so the order of
   iterated elements may not be stable.
   
   The type `Iterable<Element,Null>`, usually abbreviated
   `{Element*}` represents a possibly-empty iterable 
   container. The type `Iterable<Element,Nothing>`, 
   usually abbreviated `{Element+}` represents a nonempty 
   iterable container.
   
   An instance of `Iterable` may be constructed by 
   surrounding a value list in braces:
   
       {String+} words = { "hello", "world" };
   
   An instance of `Iterable` may be iterated using a `for`
   loop:
   
       for (c in "hello world") { ... }
   
   `Iterable` and its subtypes define various operations
   that return other iterable objects. Such operations 
   come in two flavors:
   
   - _Lazy_ operations return a *view* of the receiving
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
   
       string.filter((Character c) => c.letter)
             .map((Character c) => c.uppercased)
   
   is much less expensive than:
   
       string.select((Character c) => c.letter)
             .collect((Character c) => c.uppercased)
   
   Furthermore, it is always easy to produce a new 
   immutable iterable object given the view produced by a
   lazy operation. For example:
   
       [ *string.filter((Character c) => c.letter)
             .map((Character c) => c.uppercased) ]
   
   Lazy operations normally return an instance of 
   `Iterable` or `Map`.
   
   However, there are certain scenarios where an eager 
   operation is more useful, more convenient, or no more 
   expensive than a lazy operation, including:
   
   - sorting operations, which are eager by nature,
   - operations which preserve emptiness/nonemptiness of
     the receiving iterable object.
   
   Eager operations normally return a sequence."""
see (`interface Collection`)
by ("Gavin")
shared interface Iterable<out Element, out Absent=Null>
        satisfies Category
        given Absent satisfies Null {
    
    "An iterator for the elements belonging to this 
     container."
    shared formal Iterator<Element> iterator();
    
    "Determines if the iterable object is empty, that is
     to say, if the iterator returns no elements."
    shared default Boolean empty 
            => iterator().next() is Finished;
    
    "The number of elements returned by the iterator of
     this iterable object, if the iterator terminates.
     In the case of an iterable whose elements are not
     countable, this operation never terminates."
    shared default Integer size 
            => count((Element e) => true);
    
    "Determines if this iterable object has more elements
     than the given length. This is an efficient operation 
     for iterable objects with many elements."
    see (`value size`)
    shared default Boolean longerThan(Integer length) {
        if (length<0) {
            return true;
        }
        variable value count=0;
        for (element in this) {
            if (count++==length) {
                return true;
            }
        }
        return false;
    }
    
    "Determines if this iterable object has fewer elements
     than the given length. This is an efficient operation 
     for iterable objects with many elements."
    see (`value size`)
    shared default Boolean shorterThan(Integer length) {
        if (length<=0) {
            return false;
        }
        variable value count=0;
        for (element in this) {
            if (++count==length) {
                return false;
            }
        }
        return true;
    }
    
    shared actual default Boolean contains(Object element) 
            => any(ifExists(element.equals));
    
    "The first element returned by the iterator, if any,
     or `null` if the iterable object is empty."
    shared default Absent|Element first 
            => internalFirst(this);
    
    "The last element returned by the iterator, if any,
     or `null` if the iterable object is empty. Iterable
     objects are potentially infinite, so this operation
     might never return; furthermore, this default 
     implementation iterates all elements, which might be
     very expensive."
    shared default Absent|Element last {
        variable Absent|Element e = first;
        for (x in this) {
            e = x;
        }
        return e;
    }
    
    "Returns an iterable object containing all but the 
     first element of this container."
    shared default {Element*} rest => skipping(1);
    
    "A sequence containing the elements returned by the
     iterator."
    shared default Element[] sequence => [ for (x in this) x ];
    
    "An `Iterable` containing the results of applying
     the given mapping to the elements of to this 
     container."
    see (`function collect`)
    shared default Iterable<Result,Absent> map<Result>(
            "The mapping to apply to the elements."
            Result collecting(Element elem)) 
            => { for (elem in this) collecting(elem) };
    
    /*shared default Callable<{Result*},Args> spread<Result,Args>(
            Callable<Result,Args> method(Element elem)) 
            given Args satisfies Anything[] =>
                flatten((Args args) => 
                    { for (elem in this) unflatten(method(elem))(args) });*/
    
    "An `Iterable` containing the elements of this 
     container that satisfy the given predicate."
    see (`function select`)
    shared default {Element*} filter(
            "The predicate the elements must satisfy."
            Boolean selecting(Element elem)) 
            => { for (elem in this) if (selecting(elem)) elem };
    
    "The result of applying the accumulating function to 
     each element of this container in turn." 
    shared default Result fold<Result>(Result initial,
            "The accumulating function that accepts an
             intermediate result, and the next element."
            Result accumulating(Result partial, Element elem)) {
        variable value partial = initial;
        for (elem in this) {
            partial = accumulating(partial, elem);
        }
        return partial;
    }
    
    "The result of applying the accumulating function to 
     each element of this container in turn." 
    shared default Result|Element|Absent reduce<Result>(
            "The accumulating function that accepts an
             intermediate result, and the next element."
            Result accumulating(Result|Element partial, Element elem)) {
        value initial = first;
        if (!empty, is Element initial) {
            variable Result|Element partial = initial;
            for (elem in rest) {
                partial = accumulating(partial, elem);
            }
            return partial;
        }
        else {
            return initial;
        }
    }
    
    "The first element which satisfies the given 
     predicate, if any, or `null` otherwise."
    shared default Element? find(
            "The predicate the element must satisfy."
            Boolean selecting(Element elem)) {
        for (e in this) {
            if (selecting(e)) {
                return e;
            }
        }
        return null;
    }
    
    "The last element which satisfies the given
     predicate, if any, or `null` otherwise."
    shared default Element? findLast(
            "The predicate the element must satisfy."
            Boolean selecting(Element elem)) {
        variable Element? last = null;
        for (e in this) {
            if (selecting(e)) {
                last = e;
            }
        }
        return last;
    }
    
    "A sequence containing the elements of this
     container, sorted according to a function 
     imposing a partial order upon the elements.
     
     For convenience, the functions `byIncreasing()` 
     and `byDecreasing()` produce a suitable 
     comparison function:
     
         \"Hello World!\".sort(byIncreasing((Character c) => c.lowercased))
     
     This operation is eager by nature."
    see (`function byIncreasing`, `function byDecreasing`)
    shared default Element[] sort(
            "The function comparing pairs of elements."
            Comparison comparing(Element x, Element y)) 
            => internalSort(comparing, this);
    
    "A sequence containing the results of applying the
     given mapping to the elements of this container. An 
     eager counterpart to `map()`."
    see (`function map`)
    shared default Result[] collect<Result>(
            "The transformation applied to the elements."
            Result collecting(Element element)) 
            => map(collecting).sequence;
    
    "A sequence containing the elements of this 
     container that satisfy the given predicate. An 
     eager counterpart to `filter()`."
    see (`function filter`)
    shared default Element[] select(
            "The predicate the elements must satisfy."
            Boolean selecting(Element element)) 
             => filter(selecting).sequence;
    
    "Return `true` if at least one element satisfies the
     predicate function."
    shared default Boolean any(
            "The predicate that at least one element 
             must satisfy."
            Boolean selecting(Element e)) {
        for (e in this) {
            if (selecting(e)) {
                return true;
            }
        }
        return false;
    }
    
    "Return `true` if all elements satisfy the predicate
     function."
    shared default Boolean every(
            "The predicate that all elements must 
             satisfy."
            Boolean selecting(Element e)) {
        for (e in this) {
            if (!selecting(e)) {
                return false;
            }
        }
        return true;
    }
    
    "Produce an `Iterable` containing the elements of
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
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    variable value i=0;
                    while (i++<skip && 
                            !iter.next() is Finished) {}
                    return iter;
                }
            }
            return iterable;
        }
    }
    
    "Produce an `Iterable` containing the first `take`
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
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    object iterator satisfies Iterator<Element> {
                        variable value i=0;
                        actual shared Element|Finished next() {
                            return ++i>take then finished 
                                    else iter.next();
                        }
                    }
                    return iterator;
                }
                shared actual Element? first => outer.first;
            }
            return iterable;
        }
    }
    
    "Produce an `Iterable` containing the elements of
     this iterable object, after skipping the leading 
     elements until the given predicate function returns
     `false`."
    shared default {Element*} skippingWhile(Boolean skip(Element elem)) {
        object iterable satisfies {Element*} {
            shared actual Iterator<Element> iterator() {
                value iter = outer.iterator();
                while (!is Finished elem=iter.next()) {
                    if (!skip(elem)) {
                        object iterator satisfies Iterator<Element> {
                            variable Boolean first=true;
                            actual shared Element|Finished next() {
                                if (first) {
                                    first=false;
                                    return elem;
                                }
                                else {
                                    return iter.next();
                                }
                            }
                        }
                        return iterator;
                    }
                }
                return emptyIterator;
            }
        }
        return iterable;
    }
    
    "Produce an `Iterable` containing the leading elements 
     of this iterable object until the given predicate 
     function returns `false`."
    shared default {Element*} takingWhile(Boolean take(Element elem)) {
        object iterable satisfies {Element*} {
            shared actual Iterator<Element> iterator() {
                value iter = outer.iterator();
                object iterator satisfies Iterator<Element> {
                    variable Boolean alive = true;
                    actual shared Element|Finished next() {
                        if (alive, !is Finished next = iter.next()) {
                            if (take(next)) {
                                return next;
                            }
                            else {
                                alive = false;
                            }
                        }
                        return finished;
                    }
                }
                return iterator;
            }
        }
        return iterable;
    }
    
    "Produce an `Iterable` containing every `step`th 
     element of this iterable object. If the step size 
     is `1`, the `Iterable` contains the same elements 
     as this iterable object. The step size must be 
     greater than zero. The expression
     
         (0..10).by(3)
     
     results in an iterable object with the elements
     `0`, `3`, `6`, and `9` in that order."
    throws (`class AssertionException`, 
            "if the given step size is nonpositive, 
             i.e. `step<1`")
    shared default Iterable<Element,Absent> by(Integer step) {
        "step size must be greater than zero"
        assert (step > 0);
        if (step == 1) {
            return this;
        } 
        else {
            object iterable satisfies Iterable<Element,Absent> {
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    object iterator satisfies Iterator<Element> {
                        shared actual Element|Finished next() {
                            value next = iter.next();
                            variable value i=0;
                            while (++i<step && 
                                    !iter.next() is Finished) {}
                            return next;
                        }
                    }
                    return iterator;
                }
            }
            return iterable;
        }
    }
    
    "Return the number of elements in this `Iterable` 
     that satisfy the predicate function."
    shared default Integer count(
            "The predicate satisfied by the elements to
             be counted."
            Boolean selecting(Element element)) {
        variable value count=0;
        for (elem in this) {
            if (exists elem, 
                selecting(elem)) {
                count++;
            }
        }
        return count;
    }
    
    "The non-null elements of this `Iterable`, in their
     original order. For null elements of the original 
     `Iterable`, there is no entry in the resulting 
     iterable object."
    shared default {Element&Object*} coalesced 
            => { for (e in this) if (exists e) e };
    
    "All entries of form `index->element` where `index` 
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
        object indexes
                satisfies {<Integer->Element&Object>*} {
            value orig => outer;
            shared actual Iterator<Integer->Element&Object> iterator() {
                object iterator satisfies Iterator<Integer->Element&Object> {
                    value iter = orig.iterator();
                    variable value i=0;
                    shared actual <Integer->Element&Object>|Finished next() {
                        variable value next = iter.next();
                        while (!next exists) {
                            i++;
                            next = iter.next();
                        }
                        assert (exists n = next);
                        if (!is Finished n) {
                            return i++->n;
                        }
                        else {
                            return finished;
                        }
                    }
                }
                return iterator;
            }
        }
        return indexes;
    }
    
    "An `Iterable` with the given inital element followed 
     by the elements of this iterable object."
    shared default {Element|Other+} following<Other>(Other head) {
        //TODO: should be {leading,*outer} when that is efficient
        object cons satisfies {Element|Other+} {
            shared actual Iterator<Element|Other> iterator() {
                value iter = outer.iterator();
                object iterator satisfies Iterator<Element|Other> {
                    variable Boolean first = true;
                    shared actual Element|Other|Finished next() {
                        if (first) {
                            first=false;
                            return head;
                        }
                        else {
                            return iter.next();
                        }
                    }
                }
                return iterator;
            }
        }
        return cons;
    }
    
    "The elements of this iterable object, in their
     original order, followed by the elements of the 
     given iterable object also in their original order."
    shared default Iterable<Element|Other,Absent&OtherAbsent> 
    chain<Other,OtherAbsent>(Iterable<Other,OtherAbsent> other) 
             given OtherAbsent satisfies Null {
        object chained 
                satisfies Iterable<Element|Other,Absent&OtherAbsent> {
            shared actual Iterator<Element|Other> iterator() =>
                    ChainedIterator(outer, other);
        }
        return chained;
    }
    
    "An `Iterable` that produces the elements of this 
     iterable object, replacing every `null` element 
     with the given default value. The resulting iterable
     object does not produce the value `null`."
    shared default Iterable<Element&Object|Default,Absent>
    defaultNullElements<Default>(
            "A default value that replaces `null` elements."
            Default defaultValue)
            => { for (elem in this) elem else defaultValue };
    
    /*"Creates a Map that contains this `Iterable`'s
         elements, grouped in `Sequence`s under the
         keys provided by the grouping function."
    shared default native Map<Grouping,[Element+]> group<Grouping>(
                "A function that must return the key under
                 which to group the specified element."
                Grouping grouping(Element elem))
            given Grouping satisfies Object;*/
        
    "A string of form `\"{ x, y, z }\"` where `x`, `y`, 
     and `z` are the `string` representations of the 
     elements of this collection, as produced by the
     iterator of the collection, or the string `\"{}\"` 
     if this iterable is empty. If the number of items
     is very large only a certain amount of them might
     be shown followed by \"...\"."
    shared actual default String string {
        if (empty) {
            return "{}";
        }
        else {
            String list = commaList(taking(30));
            return "{ `` longerThan(30) 
                        then list + ", ..." 
                        else list `` }"; 
        }
    }
    
    "A non-finite iterable object that produces the elements 
     of this iterable object, repeatedly."
    see (`function cycle`)
    shared default Iterable<Element,Absent> cycled {
        object iterable satisfies Iterable<Element,Absent> {
            value orig => outer;
            shared actual Iterator<Element> iterator() {
                object iterator satisfies Iterator<Element> {
                    variable Iterator<Element> iter = emptyIterator;
                    shared actual Element|Finished next() {
                        if (!is Finished next = iter.next()) {
                            return next;
                        }
                        else {
                            iter = orig.iterator();
                            return iter.next();
                        }
                        
                    }
                }
                return iterator;
            }
        }
        return iterable;
    }
    
    "A finite iterable object that produces the elements of 
     this iterable object, repeatedly, the given number of
     times."
    see (`value cycled`, `function repeat`)
    shared default Iterable<Element,Absent> cycle(Integer times) {
        object iterable satisfies Iterable<Element,Absent> {
            value orig => outer;
            shared actual Iterator<Element> iterator() {
                object iterator satisfies Iterator<Element> {
                    variable Iterator<Element> iter = emptyIterator;
                    variable Integer count=0;
                    shared actual Element|Finished next() {
                        if (!is Finished next = iter.next()) {
                            return next;
                        }
                        else {
                            if (count<times) {
                                count++;
                                iter = orig.iterator();
                            }
                            else {
                                iter = emptyIterator;
                            }
                            return iter.next();
                        }
                        
                    }
                }
                return iterator;
            }
        }
        return iterable;
    }
    
    "Returns a list formed by repeating the elements of this
     iterable object the given number of times, or an empty 
     list if `times<=0`. An eager counterpart to `cycle()`."
    see (`function cycle`)
    shared default List<Element> repeat(Integer times) {
        value sb = SequenceBuilder<Element>();
        variable value count=0;
        while (count++<times) {
            sb.appendAll(this);
        }
        return sb.sequence;
    }
    
}

String commaList({Anything*} elements) =>
        ", ".join { for (element in elements)
                    element?.string else "null" };

Boolean ifExists(Boolean predicate(Object val))(Anything val) {
    if (exists val) {
        return predicate(val);
    }
    else {
        return false;
    }
}
