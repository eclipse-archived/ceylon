"Produces the elements of an [[Iterable]] object. For 
 example, this class has an `Iterator` that produces the 
 integers from `min` to `max`:
 
     class IntegerIterable(Integer min, Integer max) 
            satisfies {Integer+} {
        iterator() => object satisfies Iterator<Integer> {
            variable value i = min;
            next() => i<=max then i++ else finished;
        };
     }
 
 An iterator is _exhausted_ when [[next]] produces the 
 value [[finished]]."
see (`interface Iterable`)
by ("Gavin")
tagged("Streams")
shared interface Iterator<out Element> {
    "The next element, or [[finished]] if there are no more 
     elements to be iterated.
     
     Repeated invocations of `next()` for a given iterator
     must eventually produce any given element of the stream
     to which the iterator belongs. A given iterator must
     not produce the same element of the stream more often
     than the element occurs in the stream.
     
     If an invocation of `next()` for a given iterator 
     produces the value `finished`, then every future 
     invocation of `next()` for that iterator must also
     produce the value `finished`.
     
     An iterator for a nonfinite stream may never produce
     the value `finished`."
    shared formal Element|Finished next();
}
