"Given zero or more argument [[streams|iterables]], return a 
 new sequence containing all elements of every given stream.
 The elements of the resulting stream are ordered first 
 according to the stream in which they occur, and then 
 according to where they occur in that stream. If there are 
 no arguments, or if none of the argument streams contain 
 any elements, return the empty sequence.
 
 For example, the expression
 
     concatenate(1..3, [0.0], {\"hello\", \"world\"})
 
 resuts in the sequence `[1, 2, 3, 0.0, \"hello\", \"world\"]`
 which has the type `[Integer|Float|String*]`."
see (`function expand`, 
     `function Iterable.chain`,
     `function Sequential.append`)
shared Element[] concatenate<Element>(
        "The streams to concatenate."
        {Element*}* iterables) 
        => [ for (it in iterables) for (val in it) val ];

"Given a [[stream|iterables]] whose elements are also 
 streams, return a new stream with all elements of every 
 nested stream. If there are no nested streams, or if all of
 the nested streams are empty, return an empty stream.
 
 For example, the expression
 
     expand { 1..3, {5}, \"hi\" }
 
 results in the stream `{ 1, 2, 3, 5, 'h', 'i' }` which has
 the type `{Integer|Character*}`."
see (`function Iterable.flatMap`,
     `function concatenate`, 
     `function Iterable.chain`)
shared Iterable<Element,OuterAbsent|InnerAbsent>
        expand<Element,OuterAbsent,InnerAbsent>
        (Iterable<Iterable<Element,InnerAbsent>,OuterAbsent> iterables)
        given OuterAbsent satisfies Null
        given InnerAbsent satisfies Null
        => { for (it in iterables) for (val in it) val };

"Given one or more argument [[streams|iterables]], return a
 stream containing elements of the given streams. The 
 elements are ordered first according to their position in 
 the argument stream, and then according to the stream in 
 which they occur. The resulting stream contains exactly the 
 same number of elements from each stream.
 
 For example, the expression
 
     interleave(1..5, \"-+\".cycled)
 
 results in the stream 
 `{ 1, '-', 2, '+', 3, '-', 4, '+', 5, '-' }`."
shared Iterable<Element,Absent>
        interleave<Element,Absent>
        (Iterable<Element,Absent>+ iterables) 
        given Absent satisfies Null {
    object interleaved satisfies Iterable<Element,Absent> {
        size => min { for (it in iterables) it.size } * iterables.size;
        empty => package.any { for (it in iterables) it.empty };
        shared actual Iterator<Element> iterator() {
            object iterator satisfies Iterator<Element> {
                value iterators 
                        = iterables.collect((Iterable<Element> it) 
                                    => it.iterator());
                variable value which = 0;
                shared actual Element|Finished next() {
                    assert (exists iter = iterators[which]);
                    if (!is Finished next = iter.next()) {
                        if (++which>=iterators.size) {
                            which = 0;
                        }
                        return next;
                    }
                    else {
                        return finished;
                    }
                }
            }
            return iterator;
        }
    }
    return interleaved;
}