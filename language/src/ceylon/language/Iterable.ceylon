"""Abstract supertype of [[categories|Category]] whose
   elements may be iterated. Iterable categories are often
   called _streams_. A stream need not be finite, but its
   elements must be countable. That is, for any given
   element of the stream, every iterator of the stream must
   eventually return the element, even if the iterator
   itself is not exhaustible.
   
   A stream may have null elements. That is, an iterator for
   the stream may produce the value [[null]] one or more
   times. For every non-null `element` of a given stream
   `it`, the expression `element in it` must evaluate to
   `true`. Thus, a stream is a category of its non-null
   elements.
   
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
   
   A given stream may not be _finite_, in which case an
   iterator for the stream is never exhaustible, and certain
   operations of this interface either never terminate or
   result in an [[AssertionError]]. It may not, in general,
   be possible to even determine if an `Iterable` is finite.
   
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
   
       string.filter((c) => c.letter||c.digit)
             .map(Character.uppercased)
   
   is much less expensive than:
   
       string.select((c) => c.letter||c.digit)
             .collect(Character.uppercased)
   
   Furthermore, it is always easy to produce a new 
   immutable iterable object given the view produced by a
   lazy operation. For example:
   
       [ *string.filter((c) => c.letter||c.digit)
                .map(Character.uppercased) ]
   
   However, there are certain scenarios where an eager 
   operation is more useful, more convenient, or no more 
   expensive than a lazy operation, including:
   
   - sorting operations, for example [[sort]], which are 
     eager by nature,
   - operations which result in a subset or subrange of the 
     receiving stream, where structural sharing would or
     could result in unnecessary memory retention.
   
   Certain operations come in both lazy and eager flavors,
   for example:
   
   - [[map]] vs [[collect]],
   - [[filter]] vs [[select]],
   - [[List.sublist]] vs [[List.measure]].
   
   Lazy operations normally return an instance of `Iterable`, 
   or even a [[List]], [[Map]], or [[Set]]. Eager operations 
   usually return a [[sequence|Sequential]]. The method
   [[sequence]] materializes the current elements of a
   stream into a sequence.
   
   There is no meaningful generic definition of equality for 
   streams. For some streams&mdash;for example, 
   `List`s&mdash;order is significant; for others&mdash;for 
   example, `Set`s&mdash;order is not significant. Therefore, 
   unlike [[Collection]], `Iterable` does not define or 
   require any form of [[value equality|Object.equals]], and 
   some streams do not support value equality. It follows 
   that the `==` operator should not be used to compare 
   generic streams, unless the streams are known to share
   some additional structure.
   
   To compare two streams, taking order into account, use
   the function [[corresponding]].
   
       {Float*} xs = ... ;
       {Float*} ys = ... ;
       Boolean same = corresponding(xs, ys);"""
see (`interface Collection`, `function corresponding`)
by ("Gavin")
shared interface Iterable<out Element=Anything, 
                          out Absent=Null>
        satisfies Category<>
        given Absent satisfies Null {
    
    "An iterator for the elements belonging to this stream."
    shared formal Iterator<Element> iterator();
    
    "Returns `true` if the iterator for this stream produces
     the given element, or `false` otherwise. In the case of 
     an infinite stream, this operation might never terminate;
     furthermore, this default implementation iterates all
     the elements until found (or not), which might be very
     expensive."
    shared actual default Boolean contains(Object element) 
            => any((e) => if (exists e) then e==element else false);
    
    "Determines if the stream is empty, that is to say, if 
     the iterator returns no elements."
    shared default Boolean empty 
            => iterator().next() is Finished;
    
    "The number of elements returned by the [[iterator]] of 
     this stream, if the iterator terminates. In the case of 
     an infinite stream, this operation never terminates."
    shared default Integer size => count((e) => true);
    
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
    
    "The [[index]]th element returned by an iterator of this 
     stream, or `null` if there are fewer than `index+1`
     elements in the stream. For a stream with an unstable 
     iteration order, a different value might be produced 
     each time `getFromFirst(index)` is called for a given
     integer `index`."
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
    
    "A [[sequence|Sequential]] containing all the elements 
     of this stream, in the same order they occur in this
     stream. This operation eagerly evaluates and collects 
     every element of the stream."
    shared default Element[] sequence()
            => let (array = Array(this)) 
                if (array.empty)
                    then []
                    else ArraySequence(array);
    
    "A stream containing all but the first element of this 
     stream. For a stream with an unstable iteration order, 
     a different stream might be produced each time `rest` 
     is evaluated.
     
     Therefore, if the stream `i` has an unstable iteration
     order, the stream `{ i.first, *i.rest }` might not have
     the same elements as `i`."
    see (`value first`)
    shared default {Element*} rest => skip(1);
    
    "A stream containing all but the last element of this 
     stream. For a stream with an unstable iteration order, 
     a different stream might be produced each time 
     `exceptLast` is evaluated."
    shared default {Element*} exceptLast
            => object 
            satisfies {Element*} {
        iterator()
                => let (iter = outer.iterator()) 
            object satisfies Iterator<Element> {
                variable value current = iter.next();
                shared actual Element|Finished next() {
                    if (!is Finished next = iter.next()) {
                        value result = current;
                        current = next;
                        return result;
                    }
                    else {
                        return finished;
                    }
                }
            };
    };
    
    "Call the given [[function|step]] for each element of 
     this stream, passing the elements in the order they 
     occur in this stream.
     
     For example:
     
         words.each(void (word) {
             print(word.lowercased);
             print(word.uppercased);
         });
     
     Has the same effect as the following `for` loop:
     
         for (word in words) {
             print(word.lowercased);
             print(word.uppercased);
         }
     
     _For certain streams this method is highly efficient,
     surpassing the performance of `for` loops on the JVM.
     Thus, `each()` is sometimes preferred in highly 
     performance-critical low-level code._"
    shared default void each(
        "The function to be called for each element in the
         stream."
        void step(Element element)) {
        for (element in this) {
            step(element);
        }
    }
    
    "Produces a stream containing the results of applying 
     the given [[mapping|collecting]] to the elements of 
     this stream.
     
     For any empty stream, `map()` returns an empty stream:
     
         {}.map(f) == {}
     
     For any nonempty stream `it`, and mapping function `f`,
     the result of `map()` may be obtained according to this
     recursive definition:
     
         it.map(f).first == f(it.first)
         it.map(f).rest == it.rest.map(f)
     
     Alternatively, and in practice, `map()` may be defined 
     by this comprehension:
     
         it.map(f) == { for (e in it) f(e) }
     
     For example, the expression
     
         (0..4).map(10.power)
     
     results in the stream `{ 1, 10, 100, 1000, 10000 }`."
    see (`function collect`)
    shared default 
    Iterable<Result,Absent> map<Result>(
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
    shared default 
    Iterable<Result,Absent|OtherAbsent>
    flatMap<Result,OtherAbsent>(
            "The mapping function to apply to the elements 
             of this stream, that produces a new stream of 
             [[Result]]s."
            Iterable<Result,OtherAbsent> collecting(Element element)) 
            given OtherAbsent satisfies Null
            => expand(map(collecting));
    
    "Produces a stream containing the elements of this 
     stream that satisfy the given [[predicate 
     function|selecting]].
     
     For any empty stream, `filter()` returns an empty 
     stream:
     
         {}.filter(p) == {}
     
     For any nonempty stream `it`, and predicate `p`, the 
     result of `filter()` may be obtained according to this
     recursive definition:
     
         it.filter(p) == { if (p(it.first)) it.first }.chain(it.rest.filter(f))
     
     Alternatively, and in practice, `filter()` may be 
     defined by this comprehension:
     
         it.filter(p) == { for (e in it) if (p(e)) e };
     
     For example, the expression
     
         (1..100).filter(13.divides)
     
     results in the stream `{ 13, 26, 39, 52, 65, 78, 91 }`."
    see (`function select`)
    shared default 
    {Element*} filter(
            "The predicate the elements must satisfy."
            Boolean selecting(Element element)) 
            => { for (elem in this) if (selecting(elem)) elem };
    
    "Produces a stream containing the elements of this 
     stream that are instances of the given [[type|Type]].
     
     For example, the expression
     
         { 1, 2, null, 3 }.narrow<Object>()
     
     results in the stream `{ 1, 2, 3 }` of type `{Integer*}`.
     
     If the type argument `Type` is not explicitly specified,
     [[Nothing]] is inferred, and the resulting stream is 
     empty."
    shared default
    {Element&Type*} narrow<Type>() 
            => { for (elem in this) if (is Type elem) elem };
    
    "Beginning with a given [[initial value|initial]], apply 
     the given [[combining function|accumulating]] to each 
     element of this stream in turn, progressively 
     accumulating a single result.
     
     For an empty stream, `fold()` returns the given initial 
     value `z`:
     
         {}.fold(z)(f) == z
     
     For a given nonempty stream `it`, initial value `z`, 
     and combining function `f`, the result of `fold()` is 
     obtained according to the following recursive 
     definition:
     
         it.fold(z)(f) == f(it.exceptLast.fold(z)(f), it.last)
     
     For example, the expression
     
         (1..100).fold(0)(plus<Integer>)
     
     results in the integer `5050`."
    see (`function reduce`, 
         `function scan`)
    shared default 
    Result fold<Result>(Result initial)
           ("The accumulating function that accepts an
             intermediate result, and the next element."
            Result accumulating(Result partial, 
                                Element element)) {
        variable value partial = initial;
        for (elem in this) {
            partial = accumulating(partial, elem);
        }
        return partial;
    }
    
    "Beginning with the [[first]] element of this stream,
     apply the given [[combining function|accumulating]] to 
     each element of this stream in turn, progressively
     accumulating a single result.
     
     For an empty stream, `reduce()` always returns `null`.
     
     For a stream with one element, `reduce()` returns that
     element:
     
         { first }.reduce(f) == first
     
     For a given stream `it` with more than one element, 
     and combining function `f`, the result of `reduce()` is 
     obtained according to the following recursive 
     definition:
     
         it.reduce(f) == f(it.exceptLast.reduce(f), it.last)
     
     For example, the expression
     
         (1..100).reduce(plus<Integer>)
     
     results in the integer `5050`." 
    see (`function fold`)
    shared default 
    Result|Element|Absent reduce<Result>(
            "The accumulating function that accepts an
             intermediate result, and the next element."
            Result accumulating(Result|Element partial, 
                                Element element)) {
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
    
    "The stream of intermediate results obtained by 
     beginning with a given [[initial value|initial]] and
     iteratively applying the given 
     [[combining function|accumulating]] to each element of 
     this stream in turn.
     
     For an empty stream, `scan()` returns a stream 
     containing just the given initial value `z`:
     
         {}.scan(z)(f) == { z }
     
     For a given nonempty stream `it`, initial value `z`, 
     and combining function `f`, the result of `scan()` is 
     obtained according to the following recursive 
     definition:
     
         it.scan(z)(f).last == f(it.exceptLast.scan(z)(f).last, it.last)
         it.scan(z)(f).exceptLast == it.exceptLast.scan(z)(f)
     
     The following identities explain the relationship 
     between `scan` and [[fold]]:
     
         it.scan(z)(f).getFromFirst(n) == it.taking(n).fold(z)(f)
         it.scan(z)(f).last == it.fold(z)(f)
         it.scan(z)(f).first == {}.fold(z)(f) == z
     
     For example, the expression
     
         (1..4).scan(0)(plus<Integer>)
     
     results in the stream `{ 0, 1, 3, 6, 10 }`."
    see (`function fold`)
    shared default 
    {Result+} scan<Result>(Result initial)
           ("The accumulating function that accepts
             the running total and the next element."
            Result accumulating(Result partial, 
                                Element element))
            => object 
            satisfies {Result+} {
        empty => false;
        first => initial;
        size => 1 + outer.size;
        iterator() 
                => let (iter = outer.iterator()) 
            object satisfies Iterator<Result> {
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
                string => outer.string + ".iterator()";
            };
    };
    
    "The first element of this stream which satisfies the 
     [[given predicate function|selecting]], if any, or 
     `null` if there is no such element. For an infinite 
     stream, this method might not terminate.
     
     For example, the expression
     
         (-10..10).find(Integer.positive)
     
     evaluates to `1`."
    shared default 
    Element? find(
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
     `null` if there is no such element. For an infinite 
     stream, this method will not terminate.
     
     For example, the expression
     
         (-10..10).findLast(3.divides)
     
     evaluates to `9`."
    shared default 
    Element? findLast(
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
    
    "The first element of this stream which satisfies the 
     [[given predicate function|selecting]], if any, 
     together with its position in the stream, or `null` 
     if there is no such element. For an infinite stream, 
     this method might not terminate.
     
     For example, the expression
     
         (-10..10).locate(Integer.positive)
     
     evaluates to `11->1`."
    shared default 
    <Integer->Element>? locate(
        "The predicate the element must satisfy."
        Boolean selecting(Element&Object element)) {
        variable value index = 0;
        for (elem in this) {
            if (exists elem, selecting(elem)) {
                return index->elem;
            }
            index++;
        }
        return null;
    }
    
    "The last element of this stream which satisfies the 
     [[given predicate function|selecting]], if any, 
     together with its position in the stream, or `null` 
     if there is no such element. For an infinite stream, 
     this method might not terminate.
     
     For example, the expression
     
         (-10..10).locateLast(3.divides)
     
     evaluates to `19->9`."
    shared default 
    <Integer->Element>? locateLast(
        "The predicate the element must satisfy."
        Boolean selecting(Element&Object element)) {
        variable <Integer->Element>? last = null;
        variable value index = 0;
        for (elem in this) {
            if (exists elem, selecting(elem)) {
                last = index->elem;
            }
            index++;
        }
        return last;
    }
    
    "Return the largest value in the stream, as measured by
     the given [[comparator function|comparing]] imposing a 
     partial order upon the elements of the stream, or `null`
     if this stream is empty.
     
     For example, the expression
     
         {-10.0, -1.0, 5.0}.max(byIncreasing(Float.magnitude))
     
     evaluates to `-10`.
     
     For any nonempty stream `it`, and comparator function 
     `c`, `it.max(c)` evaluates to the first element of `it` 
     such that for every element `e` of `it`, 
     `c(e, it.max(c)) != larger`.
     
     Note that the toplevel functions [[ceylon.language::max]]
     and [[ceylon.language::min]] may be used to find the  
     largest and smallest values in a stream of [[Comparable]] 
     values, according to the natural order of its elements."
    shared default 
    Element|Absent max(
            "The function comparing pairs of elements."
            Comparison comparing(Element x, Element y)) {
        value it = iterator();
        if (!is Finished first = it.next()) {
            variable value max = first;
            while (!is Finished val = it.next()) {
                if (comparing(val,max)==larger) {
                    max = val;
                }
            }
            return max;
        }
        else {
            "iterable must be empty"
            assert (is Absent null);
            return null;
        }
    }
    
    "Given a [[method]] of the element type [[Element]], 
     return a function that, when supplied with a list of 
     method arguments, produces a new iterable object that 
     applies the `method` to each element of this iterable 
     object in turn.
     
         {Boolean+}(Object) fun = (-1..1).spread(Object.equals);
         print(fun(0)); //prints { false, true, false }"
    shared default 
    Iterable<Result,Absent>(*Args) 
    spread<Result,Args>(Result(*Args) method(Element element))
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
     [[byDecreasing]] produce suitable comparator functions.
     
     For example, this expression
     
         \"Hello World!\".sort(byIncreasing(Character.lowercased))
     
     evaluates to the sequence 
     `[ , !, d, e, H, l, l, l, o, o, r, W].`
     
     This operation is eager by nature.
     
     Note that the toplevel function [[ceylon.language::sort]] 
     may be used to sort a stream of [[Comparable]] values 
     according to the natural order of its elements."
    see (`function increasing`, `function decreasing`,
         `function byIncreasing`, `function byDecreasing`)
    shared default 
    Element[] sort(
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
     results of applying the given [[mapping|collecting]] to
     the elements of this stream.
     
     This operation is an eager counterpart to [[map]]. For
     any stream `it`, and mapping `f`:
     
         it.collect(f) == [*it.map(f)]"
    see (`function map`)
    shared default 
    Result[] collect<Result>(
            "The transformation applied to the elements."
            Result collecting(Element element)) 
            => map(collecting).sequence();
    
    "Produce a new [[sequence|Sequential]] containing all 
     elements of this stream that satisfy the given 
     [[predicate function|selecting]], in the order in 
     which they occur in this stream.
     
     This operation is an eager counterpart to [[filter]]. 
     For any stream `it`, and predicate `p`:
     
         it.select(p) == [*it.filter(p)]"
    see (`function filter`)
    shared default 
    Element[] select(
            "The predicate the elements must satisfy."
            Boolean selecting(Element element)) 
             => filter(selecting).sequence();
    
    "Produces the number of elements in this stream that 
     satisfy the [[given predicate function|selecting]].
     For an infinite stream, this method never terminates."
    shared default 
    Integer count(
        "The predicate satisfied by the elements to be 
         counted."
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
     stream that satisfies the given [[predicate 
     function|selecting]]. If the stream is empty, returns 
     `false`. For an infinite stream, this operation might 
     not terminate."
    see (`function every`)
    shared default 
    Boolean any(
            "The predicate that at least one element must 
             satisfy."
            Boolean selecting(Element element)) {
        for (e in this) {
            if (selecting(e)) {
                return true;
            }
        }
        return false;
    }
    
    "Determines if all elements of this stream satisfy the 
     given [[predicate function|selecting]]. If the stream
     is empty, return `true`. For an infinite stream, this 
     operation might not terminate."
    see (`function any`)
    shared default 
    Boolean every(
            "The predicate that all elements must satisfy."
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
    shared default 
    {Element*} skip(Integer skipping) {
        if (skipping <= 0) {
            return this;
        }
        else {
            return object 
                    satisfies {Element*} {
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    variable value i=0;
                    while (i++<skipping &&
                            !iter.next() is Finished) {}
                    return iter;
                }
                string => outer.string + ".iterator()";
            };
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
    shared default 
    {Element*} take(Integer taking) {
        if (taking <= 0) {
            return {}; 
        }
        else {
            return object 
                    satisfies {Element*} {
                shared actual Iterator<Element> iterator() {
                    value iter = outer.iterator();
                    return object
                            satisfies Iterator<Element> {
                        variable value i=0;
                        next() => ++i>taking
                                    then finished
                                    else iter.next();
                        string => outer.string + ".iterator()";
                    };
                }
                first => outer.first;
            };
        }
    }
    
    "Produces a stream containing the elements of this 
     stream, after skipping the leading elements until the 
     given [[predicate function|skipping]] returns `false`."
    see (`function skip`,
         `function takeWhile`)
    shared default 
    {Element*} skipWhile(
            "The function that returns `false` when the 
             resulting stream should stop skipping
             elements from the stream."
            Boolean skipping(Element element)) 
            => object 
            satisfies {Element*} {
        shared actual Iterator<Element> iterator() {
            value iter = outer.iterator();
            while (!is Finished elem = iter.next()) {
                if (!skipping(elem)) {
                    return object 
                            satisfies Iterator<Element> {
                        variable Boolean first=true;
                        actual shared Element|Finished next() {
                            if (first) {
                                first = false;
                                return elem;
                            }
                            else {
                                return iter.next();
                            }
                        }
                        string => outer.string + ".iterator()";
                    };
                }
            }
            return emptyIterator;
        }
    };
    
    "Produces a stream containing the leading elements of 
     this stream until the given [[predicate function|taking]]
     returns `false`."
    see (`function take`,
         `function skipWhile`)
    shared default 
    {Element*} takeWhile(
            "The function that returns `false` when the 
             resulting stream should stop taking elements
             from this stream."
            Boolean taking(Element element)) 
            => object 
            satisfies {Element*} {
        iterator() 
                => let (iter = outer.iterator()) 
            object satisfies Iterator<Element> {
                variable Boolean alive = true;
                actual shared Element|Finished next() {
                    if (alive,
                        !is Finished next = iter.next()) {
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
            };
    };
    
    "Produces a stream formed by repeating the elements of 
     this stream the given [[number of times|times]], or an 
     empty stream if `times<=0`.
     
     For example, the expression
     
         { 1, 2 }.repeat(3)
     
     evaluates to the stream `{ 1, 2, 1, 2, 1, 2 }`."
    see (`value cycled`)
    shared default 
    {Element*} repeat(Integer times) 
            => object 
            satisfies {Element*} {
        size => times * outer.size;
        string => "(``outer.string``).repeat(``times``)";
        iterator() => CycledIterator(outer,times);
    };
    
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
    shared default 
    Iterable<Element,Absent> by(Integer step) {
        "step size must be greater than zero"
        assert (step > 0);
        if (step == 1) {
            return this;
        } 
        else {
            return object
                    satisfies Iterable<Element,Absent> {
                string => "(``outer.string``).by(``step``)";
                iterator() 
                        => let (iter = outer.iterator()) 
                    object satisfies Iterator<Element> {
                        shared actual Element|Finished next() {
                            value next = iter.next();
                            variable value i=0;
                            while (++i<step && 
                                !iter.next() is Finished) {}
                            return next;
                        }
                        string => outer.string + ".iterator()";
                    };
            };
        }
    }
    
    "Produces a stream containing the elements of this 
     stream, in the order in which they occur in this stream, 
     after replacing every `null` element in the stream with 
     the [[given default value|defaultValue]]. The value 
     `null` does not ocur in the resulting stream.
     
     For example, the expression
     
         { \"123\", \"abc\", \"456\" }.map(parseInteger).defaultNullElements(0)
     
     results in the stream `{ 123, 0, 456 }`."
    see (`value coalesced`)
    shared default
    Iterable<Element&Object|Default,Absent>
    defaultNullElements<Default>(
            "A default value that replaces `null` elements."
            Default defaultValue)
            given Default satisfies Object
            => { for (elem in this) elem else defaultValue };
    
    "The non-null elements of this stream, in the order in
     which they occur in this stream. For null elements of 
     the original stream, there is no entry in the resulting 
     stream.
     
     For example, the expression
     
         { \"123\", \"abc\", \"456\"}.map(parseInteger).coalesced
     
     results in the stream `{ 123, 456 }`."
    see (`function defaultNullElements`)
    shared default
    {Element&Object*} coalesced
            => { for (e in this) if (exists e) e };
    
    "A stream containing all [[entries|Entry]] of form
     `index->element` where `element` is an element of this
     stream, and `index` is the position at which `element`
     occurs in this stream, ordered by increasing `index`.
     
     For example, the expression
     
         { \"hello\", null, \"world\" }.indexed
     
     results in the stream `{ 0->\"hello\", 1->null, 2->\"world\" }`."
    shared default 
    Iterable<<Integer->Element>,Absent> indexed 
            => object
            satisfies Iterable<<Integer->Element>,Absent> {
        iterator() 
                => let (iter = outer.iterator()) 
            object satisfies Iterator<Integer->Element> {
                variable value i=0;
                next() => if (!is Finished next = iter.next())
                            then i++ -> next 
                            else finished;
                string => outer.string + ".iterator()";
            };
    };
    
    "A stream containing whose elements are pairs (2-tuples)
     comprising an element of this stream paired with the 
     next element in the stream. The resulting stream has
     one fewer elements than this stream.
     
     For example, the expression
     
         (1..5).paired
     
     results in the stream 
     `{ [1, 2], [2, 3], [3, 4], [4, 5] }`.
     
     This expression determines if a stream is monotonically
     increasing:
     
         every { for ([x, y] in nums.paired) x < y }
     
     For any stable `stream`, this operation is equivalent 
     to `zipPairs(stream,stream.rest)`."
    shared default 
    {[Element,Element]*} paired 
            => object 
            satisfies {[Element,Element]*} {
        size => let (size = outer.size-1) 
                if (size<0) then 0 else size;
        empty => outer.size<2;
        iterator() 
                => let (iter = outer.iterator()) 
            object satisfies Iterator<[Element,Element]> {
                variable value previous = iter.next();
                shared actual [Element,Element]|Finished next() {
                    if (!is Finished head = previous,
                        !is Finished tip = iter.next()) {
                        previous = tip;
                        return [head, tip];
                    }
                    else {
                        return finished;
                    }
                }
            };
    };
    
    "Produces a stream of sequences of the given [[length]],
     containing elements of this stream. Each sequence in 
     the stream contains the next [[length]] elements of 
     this sequence that have not yet been assigned to a 
     previous sequence, in the same order that they occur
     in this stream. The very last sequence in the stream
     may be shorter than the given `length`.
     
     For example, the expression
     
         \"hello\".partition(2)
     
     results in the stream `{ ['h','e'], ['l','l'], ['o'] }.`
     
     For any `stream` and for any strictly positive integer 
     [[length]]:
     
         expand { stream.partition(length) } == stream"
    throws (`class AssertionError`,
            "if `length<=0`")
    shared default 
    Iterable<[Element+],Absent> partition(Integer length) {
        "length must be strictly positive"
        assert (length>0);
        return object
                satisfies Iterable<[Element+],Absent> {
            size => let (outerSize = outer.size,
                          quotient = outerSize/length)
                        if (length.divides(outerSize)) 
                            then quotient 
                            else quotient+1;
            empty => outer.empty;
            iterator() 
                    => let (iter = outer.iterator()) 
                object satisfies Iterator<[Element+]> {
                    shared actual [Element+]|Finished next() {
                        if (!is Finished next = iter.next()) {
                            value array = arrayOfSize(length, next);
                            variable value index = 0;
                            while (++index<length) {
                                if (!is Finished current = iter.next()) {
                                    array.set(index, current);
                                }
                                else {
                                    return ArraySequence(array[...index-1]);
                                }
                            }
                            return ArraySequence(array);
                        }
                        else {
                            return finished;
                        }
                    }
                };
        };
    }
    
    "Produces a stream with a given [[initial element|head]], 
     followed by the elements of this stream, in the order 
     in which they occur in this stream.
     
     For example, the expression
     
         (1..3).follow(0)
     
     evaluates to the stream `{ 0, 1, 2, 3 }`.
     
     Note that the expression `stream.follow(head)` eagerly 
     evaluates `head`, and therefore is not precisely the 
     same as this enumeration expression, where `head` is
     evaluated lazily:
     
         { head, *stream }"
    see (`function chain`)
    shared default 
    {Element|Other+} follow<Other>(Other head) 
            => { head, *this };
    
    "The elements of this stream, in the order in which they 
     occur in this stream, followed by the elements of the 
     [[given stream|other]] in the order in which they occur 
     in the given stream.
     
     For example, the expression
     
         (1..3).chain(\"abc\")
     
     evaluates to the stream `{ 1, 2, 3, 'a', 'b', 'c' }`."
    see (`function expand`)
    shared default 
    Iterable<Element|Other,Absent&OtherAbsent> 
    chain<Other,OtherAbsent>
            (Iterable<Other,OtherAbsent> other) 
            given OtherAbsent satisfies Null 
            => object 
            satisfies Iterable<Element|Other,
                               Absent&OtherAbsent> {
        iterator() => ChainedIterator(outer, other);
    };
    
    "A stream of pairs of elements of this stream and the 
     the given stream, where for each element `x` of this
     stream, and element `y` of the given stream, the
     pair `[x,y]` belongs to the resulting stream. The pairs
     are sorted first by the position of `x` in this stream,
     and then by the position of `y` in the given stream.
     
     For example, this expression
     
         (1..3).product(\"ab\")
     
     evaluates to the stream 
     `{ [1,'a'], [1,'b'], [2,'a'], [2,'b'], [3,'a'], [3,'b'] }`."
    shared default 
    Iterable<[Element,Other],Absent|OtherAbsent>
    product<Other,OtherAbsent>
            (Iterable<Other,OtherAbsent> other) 
            given OtherAbsent satisfies Null
            => { for (x in this) for (y in other) [x,y] };
    
    "An infinite stream that produces the elements of this 
     stream, repeatedly.
     
     For example, the expression
     
         {6, 9}.cycled.taking(5)
     
     evaluates to the stream `{ 6, 9, 6, 9, 6 }`."
    see (`function repeat`)
    shared default 
    Iterable<Element,Absent> cycled 
            => object 
            satisfies Iterable<Element,Absent> {
        value orig => outer;
        string => "(``outer.string``).cycled";
        shared actual Integer size {
            "stream is infinite" 
            assert (false); 
        }
        iterator() 
                => object satisfies Iterator<Element> {
                variable Iterator<Element> iter 
                        = emptyIterator;
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
            };
    };
    
    "A stream that contains the given [[element]] interposed
     between blocks of [[step]] elements of this stream. The
     resulting stream starts with the [[first]] element of 
     this stream and ends with the [[last]] element of this
     stream. Elements of this stream occur in the resulting
     stream in the same order they occur in this stream.
     
     For example, the expression
     
         String(\"hello\".interpose(' '))
     
     evaluates to the string `\"h e l l o\"`."
    throws (`class AssertionError`, "if `step<1`")
    see (`function interleave`)
    shared default 
    Iterable<Element|Other,Absent> interpose<Other>(
            "The value to interpose between blocks of 
             elements of this stream."
            Other element,
            "The step size that determines how often the 
             given [[element]] occurs in the resulting 
             stream. The `element` occurs after each block 
             of size `step` of elements of this stream. If 
             `step==1`, the `element` occurs at every second 
             position."
            Integer step=1) {
        "step must be strictly positive"
        assert (step>=1);
        return object
                satisfies Iterable<Element|Other,Absent> {
            shared actual Integer size {
                value outerSize = outer.size;
                return if (outerSize>0) 
                    then outerSize + (outerSize-1)/step 
                    else 0;
            }
            empty => outer.empty;
            first => outer.first;
            last => outer.last;
            iterator() 
                    => let (iter = outer.iterator()) 
                object satisfies Iterator<Element|Other> {
                    variable value current = iter.next();
                    variable value count = 0;
                    shared actual Element|Other|Finished next() {
                        if (!is Finished curr = current) {
                            if ((step+1).divides(++count)) {
                                return element;
                            }
                            else {
                                current = iter.next();
                                return curr;
                            }
                        }
                        else {
                            return finished;
                        }
                    }
                    string => outer.string + ".iterator()";
                };
        };
    }
    
    "A string of form `\"{ x, y, z }\"` where `x`, `y`, and 
     `z` are the `string` representations of the elements of 
     this collection, as produced by the iterator of the 
     stream, or the string `\"{}\"` if this stream is empty. 
     If the stream is very long, the list of elements might 
     be truncated, as indicated by an ellipse."
    shared actual default String string {
        value elements = take(31).sequence();
        if (elements.empty) {
            return "{}";
        }
        else if (elements.size==31) {
            return "{ ``commaList(elements.take(30))``, ... }";
        }
        else {
            return "{ ``commaList(elements)`` }";
        }
    }
    
}

String commaList({Anything*} elements)
        => ", ".join { for (e in elements) stringify(e) };
