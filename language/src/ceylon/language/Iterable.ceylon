"""Abstract supertype of [[categories|Category]] whose 
   elements may be iterated. Iterable categories are often 
   called _streams_. A stream need not be finite, but its 
   elements must be countable. That is, for any given 
   element of the stream, every iterator of the stream must 
   eventually return the element, even if the iterator 
   itself is not exhaustable. 
   
   A given stream might not have a well-defined order, and 
   so the order in which elements are produced by the 
   stream's iterator may not be _stable_. That is, the order 
   may be different for two different iterators of the 
   stream. However, a stream has a well-defined set of 
   elements, and any two iterators for an immutable finite 
   stream should eventually return the same elements. 
   Furthermore, any two iterators for an immutable finite
   stream should eventually return exactly the same number 
   of elements, which must be the [[size]] of the stream.
   
   The type `Iterable<Element,Null>`, usually abbreviated
   `{Element*}`, represents a possibly-empty iterable 
   container. The type `Iterable<Element,Nothing>`, usually 
   abbreviated `{Element+}`, represents a nonempty iterable 
   container.
   
   A value list in braces produces a new instance of 
   `Iterable`:
   
       {String+} words = { "hello", "world" };
   
   An instance of `Iterable` may be iterated using a `for`
   loop:
   
       for (c in "hello world") { ... }
   
   Comprehensions provide a convenient syntax for 
   transforming streams:
   
       {Integer+} lengths = { for (w in words) w.size };
   
   `Iterable` and its subtypes define various operations
   that return other iterable objects. Such operations come 
   in two flavors:
   
   - _Lazy_ operations return a *view* of the receiving
     iterable object. If the underlying iterable object is
     mutable, then changes to the underlying object will be 
     reflected in the resulting view. Lazy operations are 
     usually efficient, avoiding memory allocation or
     iteration of the receiving iterable object.
   - _Eager_ operations return an immutable object. If the
     receiving iterable object is mutable, changes to this
     object will not be reflected in the resulting immutable
     object. Eager operations are often expensive, involving
     memory allocation and iteration of the receiving 
     iterable object.
   
   Lazy operations are generally preferred, because they can 
   be efficiently chained. For example:
   
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
   
   However, there are certain scenarios where an eager 
   operation is more useful, more convenient, or no more 
   expensive than a lazy operation, including:
   
   - sorting operations, for example [[sort]], which are 
     eager by nature,
   - operations which result in a subset or subrange of the 
     receiving stream, where structural sharing would or
     could result in unecessary memory retention.
   
   Certain operations come in both lazy and eager flavors,
   for example:
   
   - [[map]] vs [[collect]],
   - [[filter]] vs [[select]],
   - [[List.sublist]] vs [[List.measure]].
   
   Lazy operations normally return an instance of `Iterable`, 
   or even a [[List]], [[Map]], or [[Set]]. Eager operations 
   usually return a [[sequence|Sequential]]. The method
   [[sequence]] materializes the current elements of a
   stream into a sequence."""
see (`interface Collection`)
by ("Gavin")
shared interface Iterable<out Element, out Absent=Null>
        satisfies Category
        given Absent satisfies Null {
    
    "An iterator for the elements belonging to this stream."
    shared formal Iterator<Element> iterator();
    
    "Determines if the stream is empty, that is to say, if 
     the iterator returns no elements."
    shared default Boolean empty 
            => iterator().next() is Finished;
    
    "The number of elements returned by the iterator of this 
     stream, if the iterator terminates. In the case of an 
     infinite stream, this operation never terminates."
    shared default Integer size 
            => count((Element e) => true);
    
    "Determines if this stream has more elements than the 
     given [[length]]. This is an efficient operation for
     streams with many elements."
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
    
    "Determines if this stream has fewer elements than the 
     given [[length]]. This is an efficient operation for 
     streams with many elements."
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
    
    "The first element returned by the iterator, if any, or 
     `null` if this stream is empty. For a stream with an
     unstable iteration order, a different value might be
     produced each time `first` is evaluated."
    shared default Absent|Element first {
        if (!is Finished first = iterator().next()) {
            return first;
        }
        else {
            "iterator for nonempty iterable must produce at 
             least one element"
            assert (is Absent null);
            return null;
        }
    }
    
    "The last element returned by the iterator, if any, or 
     `null` if this stream is empty. In the case of an 
     infinite stream, this operation never terminates;
     furthermore, this default implementation iterates all 
     elements, which might be very expensive."
    shared default Absent|Element last {
        variable Absent|Element e = first;
        for (x in this) {
            e = x;
        }
        return e;
    }
    
    "A stream containing all but the first element of this 
     stream. For a stream with an unstable iteration order, 
     a different value might be produced each time `rest` is
     evaluated.
     
     Therefore, if the stream `i` has an unstable iteration
     order, the stream `{ i.first, *i.rest }` might not have
     the same elements as `i`."
    see (`value first`)
    shared default {Element*} rest => skip(1);
    
    "A [[sequence|Sequential]] containing all the elements 
     of this stream, in the same order they occur in this
     stream. This operation eagerly evaluates and collects 
     every element of the stream."
    shared default Element[] sequence() {
        value array = Array(this);
        if (array.empty) {
            return [];
        }
        else {
            return ArraySequence(array);
        }
    }
    
    "The [[index]]th element returned by an iterator of this 
     stream, or `null` if there are fewer than `index+1`
     elements in the stream. For a stream with an unstable 
     iteration order, a different value might be produced 
     each time `getFromFirst()` is called."
    shared default Element? getFromFirst(Integer index) {
        variable value current = 0;
        for (element in this) {
            if (current++==index) {
                return element;
            }
        }
        else {
            return null;
        }
    }
    
    "Produces a stream containing the results of applying 
     the given [[mapping|collecting]] to the elements of to 
     this stream.
     
     For example, the expression
     
         (0..4).map(10.power)
     
     results in the stream `{ 1, 10, 100, 1000, 10000 }`."
    see (`function collect`)
    shared default Iterable<Result,Absent> map<Result>(
            "The mapping to apply to the elements."
            Result collecting(Element element)) 
            => { for (elem in this) collecting(elem) };
    
    "Given a [[mapping function|collecting]] that accepts an 
     [[Element]] and returns a stream of [[Result]]s, 
     produces a new stream containing all elements of every 
     `Result` stream that results from applying the function 
     to the elements of this stream.
     
     For example, the expression
     
         { \"Hello\", \"World\" }.flatMap(String.lowercased)
     
     results in this stream:
     
         { 'h', 'e', 'l', 'l', 'o', 'w', 'o', 'r,' 'l', 'd' }
     
     The expression
         
         { \"hello\"->\"hola\", \"world\"->\"mundo\" }
                 .flatMap(Entry<String,String>.pair)
     
     produces this stream:
     
         { \"hello\", \"hola\", \"world\", \"mundo\" }"
    see (`function expand`)
    shared default Iterable<Result,Absent|OtherAbsent> 
            flatMap<Result,OtherAbsent>(
            "The mapping function to apply to the elements 
             of this stream, that produces a new stream of 
             [[Result]]s."
            Iterable<Result,OtherAbsent> collecting(Element element)) 
            given OtherAbsent satisfies Null
            => expand(map(collecting));
    
    "Produces a stream containing the elements of this 
     stream that satisfy the [[given predicate 
     function|selecting]].
     
     For example, the expression
     
         (1..100).filter(13.divides)
     
     results in the stream `{ 13, 26, 39, 52, 65, 78, 91 }`."
    see (`function select`)
    shared default {Element*} filter(
            "The predicate the elements must satisfy."
            Boolean selecting(Element element)) 
            => { for (elem in this) if (selecting(elem)) elem };
    
    "The result of applying the [[given accumulating 
     function|accumulating]] to each element of this stream 
     in turn.
     
     For example, the expression
     
         (1..100).fold(0, plus<Integer>)
     
     results in the integer `5050`."
    see (`function reduce`, 
         `function scan`)
    shared default Result fold<Result>(Result initial,
            "The accumulating function that accepts an
             intermediate result, and the next element."
            Result accumulating(Result partial, Element element)) {
        variable value partial = initial;
        for (elem in this) {
            partial = accumulating(partial, elem);
        }
        return partial;
    }
    
    "The result of applying the [[given accumulating 
     function|accumulating]] to each element of this stream 
     in turn.
     
     For example, the expression
     
         (1..100).reduce(plus<Integer>)
     
     results in the integer `5050`." 
    see (`function fold`)
    shared default Result|Element|Absent reduce<Result>(
            "The accumulating function that accepts an
             intermediate result, and the next element."
            Result accumulating(Result|Element partial, Element element)) {
        value it = iterator();
        if (!is Finished initial = it.next()) {
            variable Result|Element partial = initial;
            while (!is Finished next = it.next()) {
                partial = accumulating(partial, next);
            }
            return partial;
        }
        else {
            "iterable must be empty"
            assert (is Absent null);
            return null;
        }
    }
    
    "The stream of results obtained by iteratively applying
     the given [[accumulating|accumulating]] function to
     each element of this iterable in turn.
     
         x.scan(z, f) == { z, f(z, x[0]), f(f(z, x[0]), x[1]), ... }
     
     The following identities explain the relationship 
     between `scan` and [[fold]]:
     
         x.scan(z, f)[i] == x.taking(i).fold(z, f)
         x.scan(z, f).last == x.fold(z, f)
         x.scan(z, f).first == {}.fold(z, f) == z
     
     For example, the expression
     
         (1..4).scan(0, plus<Integer>)
     
     results in the stream `{ 0, 1, 3, 6, 10 }`."
    see (`function fold`)
    shared default {Result+} scan<Result>(Result initial,
            "The accumulating function that accepts
             the running total and the next element."
            Result accumulating(Result partial, Element element)) {
        object iterable satisfies {Result+} {
            first => initial;
            shared actual Iterator<Result> iterator() {
                value iter = outer.iterator();
                object iterator satisfies Iterator<Result> {
                    variable value returnInitial = true;
                    variable value partial = initial;
                    shared actual Result|Finished next() {
                        if (returnInitial) {
                            returnInitial = false;
                            return initial;
                        }
                        else if (!is Finished element = iter.next()) {
                            partial = accumulating(partial, element);
                            return partial;
                        }
                        else {
                            return finished;
                        }
                    }
                }
                return iterator;
            }
        }
        return iterable;
    }
    
    "The first element of this stream which satisfies the 
     [[given predicate function|selecting]], if any, or 
     `null` otherwise.  For an infinite stream, this 
     operation might not terminate."
    shared default Element? find(
            "The predicate the element must satisfy."
            Boolean selecting(Element&Object element)) {
        for (elem in this) {
            if (exists elem, selecting(elem)) {
                return elem;
            }
        }
        return null;
    }
    
    "The last element of this stream which satisfies the 
     [[given predicate function|selecting]], if any, or 
     `null` otherwise. For an infinite stream, this operation 
     might not terminate."
    shared default Element? findLast(
            "The predicate the element must satisfy."
            Boolean selecting(Element&Object element)) {
        variable Element? last = null;
        for (elem in this) {
            if (exists elem, selecting(elem)) {
                last = elem;
            }
        }
        return last;
    }
    
    "Given a [[method]] of the element type [[Element]], 
     return a function that, when supplied with a list of 
     method arguments, produces a new iterable object
     that applies the `method` to each element of this 
     iterable object in turn.
     
         {Boolean+}(Object) fun = (-1..1).spread(Object.equals);
         print(fun(0)); //prints { false, true, false }"
    shared default Callable<Iterable<Result,Absent>,Args> 
    spread<Result,Args>(Callable<Result,Args> method(Element element))
            given Args satisfies Anything[]
            //=> flatten((Args args) => map(shuffle(method)(*args)));
            => flatten((Args args) 
                => { for (elem in this) method(elem)(*args) });
    
    /*"Produce a new [[sequence|Sequential]] containing the 
     elements of this stream, in the reverse order to the 
     order in which they occur in this stream.
     
     That is, if a stream `i` is stable, and if `x` and `y` 
     are elements `i`, and `x` is produced before `y` by the 
     [[iterator]] for `i`, then `y` occurs before `x` in the 
     sequence `i.reverse()`.
     
     This operation is an eager counterpart to 
     [[List.reversed]]."
    see (`value List.reversed`)
    shared default List<Element> reverse() {
        value array = Array(this);
        if (array.empty) {
            return [];
        }
        else {
            array.reverseInPlace();
            return ArraySequence(array);
        }
    }*/
    
    "Produce a new [[sequence|Sequential]] containing the 
     elements of this stream, sorted according to the given 
     [[comparator function|comparing]] imposing a partial 
     order upon the elements of the stream.
     
     For convenience, the functions [[byIncreasing]] and 
     [[byDecreasing]] produce suitable comparator functions:
    
        \"Hello World!\".sort(byIncreasing(Character.lowercased))
    
     This operation is eager by nature."
    see (`function byIncreasing`, 
         `function byDecreasing`)
    shared default Element[] sort(
            "The function comparing pairs of elements."
            Comparison comparing(Element x, Element y)) {
        value array = Array(this);
        if (array.empty) {
            return [];
        }
        else {
            array.sortInPlace(comparing);
            return ArraySequence(array);
        }
    }
    
    "Produce a new [[sequence|Sequential]] containing the 
     results of applying the [[given mapping|collecting]] to
     the elements of this stream.
     
     This operation is an eager counterpart to [[map]]."
    see (`function map`)
    shared default Result[] collect<Result>(
            "The transformation applied to the elements."
            Result collecting(Element element)) 
            => map(collecting).sequence();
    
    "Produce a new [[sequence|Sequential]] containing all 
     elements of this stream that satisfy the given 
     [[predicate function|selecting]], in the order in 
     which they occur in this stream.
     
     This operation is an eager counterpart to [[filter]]."
    see (`function filter`)
    shared default Element[] select(
            "The predicate the elements must satisfy."
            Boolean selecting(Element element)) 
             => filter(selecting).sequence();
    
    "Produces the number of elements in this stream that 
     satisfy the [[given predicate function|selecting]].
     For an infinite stream, this operation never 
     terminates."
    shared default Integer count(
        "The predicate satisfied by the elements to
         be counted."
        Boolean selecting(Element element)) {
        variable value count=0;
        for (elem in this) {
            if (selecting(elem)) {
                count++;
            }
        }
        return count;
    }
    
    "Determines if there is at least one element of this 
     stream that satisfies the [[given predicate 
     function|selecting]]. This method returns `false` for 
     empty streams. For an infinite stream, this operation 
     might not terminate."
    shared default Boolean any(
            "The predicate that at least one element 
             must satisfy."
            Boolean selecting(Element element)) {
        for (e in this) {
            if (selecting(e)) {
                return true;
            }
        }
        return false;
    }
    
    "Determines if all elements of this stream satisfy the 
     [[given predicate function|selecting]]. This method 
     returns `true` for empty streams.  For an infinite 
     stream, this operation might not terminate."
    shared default Boolean every(
            "The predicate that all elements must 
             satisfy."
            Boolean selecting(Element element)) {
        for (e in this) {
            if (!selecting(e)) {
                return false;
            }
        }
        return true;
    }
    
    "Produces a stream containing the elements of this 
     stream, after skipping the first [[skipping]] elements
     produced by its iterator.
     
     If this stream does not contain more elements than the 
     specified number of elements to skip, the resulting 
     stream has no elements. If the specified number of 
     elements to skip is zero or fewer, the resulting stream 
     contains the same elements as this stream."
    see (`function List.sublistFrom`, 
         `function skipWhile`, 
         `function take`)
    shared default {Element*} skip(Integer skipping) {
        if (skipping <= 0) {
            return this;
        }
        else {
            object iterable 
                    satisfies {Element*} {
                shared actual Iterator<Element> iterator() {
                    //TODO: iterator has wrong string
                    value iter = outer.iterator();
                    variable value i=0;
                    while (i++<skipping &&
                            !iter.next() is Finished) {}
                    return iter;
                }
            }
            return iterable;
        }
    }
    
    "Produces a stream containing the first [[taking]]
     elements of this stream.
     
     If the specified number of elements to take is larger 
     than the number of elements of this stream, the 
     resulting stream contains the same elements as this 
     stream. If the specified number of elements to take is
     fewer than one, the resulting stream has no elements."
    see (`function List.sublistTo`,
         `function List.initial`,
         `function takeWhile`,
         `function skip`)
    shared default {Element*} take(Integer taking) {
        if (taking <= 0) {
            return {}; 
        }
        else {
            object iterable 
                    satisfies {Element*} {
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    object iterator
                            satisfies Iterator<Element> {
                        variable value i=0;
                        actual shared Element|Finished next() {
                            return ++i>taking then finished
                                    else iter.next();
                        }
                        string => outer.string + ".iterator()";
                    }
                    return iterator;
                }
                shared actual Element? first => outer.first;
            }
            return iterable;
        }
    }
    
    "Produces a stream containing the elements of this 
     stream, after skipping the leading elements until the 
     given [[predicate function|skipping]] returns `false`."
    see (`function skip`,
         `function takeWhile`)
    shared default {Element*} skipWhile(
            "The function that returns `false` when the 
             resulting stream should stop skipping
             elements from the stream."
            Boolean skipping(Element element)) {
        object iterable 
                satisfies {Element*} {
            shared actual Iterator<Element> iterator() {
                value iter = outer.iterator();
                while (!is Finished elem=iter.next()) {
                    if (!skipping(elem)) {
                        object iterator 
                                satisfies Iterator<Element> {
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
                            string => outer.string + ".iterator()";
                        }
                        return iterator;
                    }
                }
                return emptyIterator;
            }
        }
        return iterable;
    }
    
    "Produces a stream containing the leading elements of 
     this stream until the given [[predicate function|taking]]
     returns `false`."
    see (`function take`,
         `function skipWhile`)
    shared default {Element*} takeWhile(
            "The function that returns `false` when the 
             resulting stream should stop taking elements
             from this stream."
            Boolean taking(Element element)) {
        object iterable 
                satisfies {Element*} {
            shared actual Iterator<Element> iterator() {
                value iter = outer.iterator();
                object iterator 
                        satisfies Iterator<Element> {
                    variable Boolean alive = true;
                    actual shared Element|Finished next() {
                        if (alive, !is Finished next = iter.next()) {
                            if (taking(next)) {
                                return next;
                            }
                            else {
                                alive = false;
                            }
                        }
                        return finished;
                    }
                    string => outer.string + ".iterator()";
                }
                return iterator;
            }
        }
        return iterable;
    }
    
    "Produces a stream formed by repeating the elements of 
     this stream the [[given number of times|times]], or an 
     empty stream if `times<=0`."
    see (`value cycled`)
    shared default {Element*} repeat(Integer times) {
        object iterable satisfies {Element*} {
            value orig => outer;
            size => times * outer.size;
            string => "(``outer.string``).repeat(``times``)";
            iterator() => CycledIterator(outer,times);
        }
        return iterable;
    }
    
    "Produces a stream containing every [[step]]th element 
     of this stream. If the step size is `1`, the resulting
     stream contains the same elements as this stream.
     
     For example, the expression
     
         (0..10).by(3)
     
     results in the stream `{ 0, 3, 6, 9 }`.
     
     The step size must be greater than zero."
    throws (`class AssertionError`, 
            "if the given step size is nonpositive, 
             i.e. `step<1`")
    shared default Iterable<Element,Absent> by(Integer step) {
        "step size must be greater than zero"
        assert (step > 0);
        if (step == 1) {
            return this;
        } 
        else {
            object iterable 
                    satisfies Iterable<Element,Absent> {
                string => "(``outer.string``).by(``step``)";
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    object iterator 
                            satisfies Iterator<Element> {
                        shared actual Element|Finished next() {
                            value next = iter.next();
                            variable value i=0;
                            while (++i<step && 
                                    !iter.next() is Finished) {}
                            return next;
                        }
                        string => outer.string + ".iterator()";
                    }
                    return iterator;
                }
            }
            return iterable;
        }
    }
    
    "Produces a stream containing the elements of this 
     stream, replacing every `null` element with the [[given 
     default value|defaultValue]]. The resulting stream does 
     not have the value `null`."
    see (`value coalesced`)
    shared default Iterable<Element&Object|Default,Absent>
            defaultNullElements<Default>(
        "A default value that replaces `null` elements."
        Default defaultValue)
            => { for (elem in this) elem else defaultValue };
    
    "The non-null elements of this stream, in the order in
     which they occur in this stream. For null elements of 
     the original stream, there is no entry in the resulting 
     stream."
    shared default {Element&Object*} coalesced 
            => { for (e in this) if (exists e) e };
    
    "All entries of form `index->element` where `index` is 
     the position at which `element` occurs, for every
     non-null element of this stream, ordered by increasing 
     `index`. For a null element at a given position in this 
     stream, there is no entry with the corresponding index 
     in the resulting stream.
     
     For example, the expression 
     
         { \"hello\", null, \"world\" }.indexed
     
     results in the stream `{ 0->\"hello\", 2->\"world\" }`."
    shared default Iterable<<Integer->Element&Object>,Element&Null|Absent> indexed {
        object indexes
                satisfies Iterable<<Integer->Element&Object>,Element&Null|Absent> {
            shared actual Iterator<Integer->Element&Object> iterator() {
                value iter = outer.iterator();
                object iterator 
                        satisfies Iterator<Integer->Element&Object> {
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
                    string => outer.string + ".iterator()";
                }
                return iterator;
            }
        }
        return indexes;
    }
    
    "For a stable stream, this operation is equivalent to
     `zipPairs(stream,stream.rest)`."
    shared default {[Element,Element]*} paired {
         object pairs satisfies {[Element,Element]*} {
             shared actual Integer size {
                 value size =  outer.size-1;
                 return size<0 then 0 else size;
             }
             shared actual Iterator<[Element, Element]> iterator() {
                 value iter = outer.iterator();
                 object iterator 
                         satisfies Iterator<[Element, Element]> {
                     variable value previous = iter.next();
                     shared actual [Element, Element]|Finished next() {
                         if (!is Finished head = previous,
                             !is Finished tip = iter.next()) {
                             previous = tip;
                             return [head, tip];
                         }
                         else {
                             return finished;
                         }
                     }
                 }
                 return iterator;
             }
         }
         return pairs;
    }
    
    "Produces a stream with a given [[initial element|head]], 
     followed by the elements of this stream, in the order 
     in which they occur in this stream. 
     
     Note that the expression `stream.follow(head)` may be 
     written as:
     
         { head, *stream }"
    see (`function chain`)
    shared default {Element|Other+} follow<Other>(Other head) 
            => { head, *this };
    
    "The elements of this stream, in the order in which they 
     occur in this stream, followed by the elements of the 
     [[given stream|other]] in the order in which they occur 
     in the given stream."
    see (`function expand`)
    shared default Iterable<Element|Other,Absent&OtherAbsent> 
    chain<Other,OtherAbsent>(Iterable<Other,OtherAbsent> other) 
             given OtherAbsent satisfies Null {
        object chained 
                satisfies Iterable<Element|Other,Absent&OtherAbsent> {
            iterator() => ChainedIterator(outer, other);
        }
        return chained;
    }
    
    //TODO: uncomment when backend bug is fixed
    /*shared default Iterable<[Element,Other],Absent|OtherAbsent>
    product<Other,OtherAbsent>(Iterable<Other,OtherAbsent> other) 
            given OtherAbsent satisfies Null
            => { for (x in this) for (y in other) [x,y] };*/
    
    "An infinite stream that produces the elements of this 
     stream, repeatedly.
     
     For example, the expression
     
         {6, 9}.cycled.taking(5)
     
     evaluates to the stream `{ 6, 9, 6, 9, 6 }`."
    see (`function repeat`)
    shared default Iterable<Element,Absent> cycled {
        object iterable satisfies Iterable<Element,Absent> {
            value orig => outer;
            string => "(``outer.string``).cycled";
            shared actual Integer size {
                "stream is infinite" 
                assert (false); 
            }
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
                    string => outer.string + ".iterator()";
                }
                return iterator;
            }
        }
        return iterable;
    }
    
   "A string of form `\"{ x, y, z }\"` where `x`, `y`, and 
     `z` are the `string` representations of the elements of 
     this collection, as produced by the iterator of the 
     stream, or the string `\"{}\"` if this stream is empty. 
     If the stream is very long, the list of elements might 
     be truncated, as indicated by an ellipse."
    shared actual default String string {
        value strings = take(31)
                .map((Element e) => e?.string else "<null>")
                .sequence();
        if (strings.empty) {
            return "{}";
        }
        else if (strings.size==31) {
            return "{ " + ", ".join(strings.take(30)) + 
                    ", ... }";
        }
        else {
            return "{ " + ", ".join(strings) + " }";
        }
    }
    
}

Boolean ifExists(Boolean predicate(Object val))(Anything val) {
    if (exists val) {
        return predicate(val);
    }
    else {
        return false;
    }
}

"Produces the [[stream|Iterable]] that results from repeated 
 application of the given [[function|next]] to the given 
 [[first]] element of the stream. The stream ends when the 
 function first returns [[finished]]. If the function never 
 returns `finished`, the stream is infinite."
shared {Element+} iterable<Element>(
        "The first element of the resulting stream."
        Element first, 
        "The function that produces the next element of the
         stream, given the current element."
        Element|Finished next(Element element)) {
    value start = first;
    object iterable satisfies {Element+} {
        first => start;
        function nextElement(Element element) => next(element);
        shared actual Iterator<Element> iterator() {
            variable Element|Finished current = start;
            object iterator satisfies Iterator<Element> {
                shared actual Element|Finished next() {
                    if (!is Finished result = current) {
                        current = nextElement(result);
                        return result;
                    }
                    else {
                        return finished;
                    }
                }
            }
            return iterator;
        }
    }
    return iterable;
}
