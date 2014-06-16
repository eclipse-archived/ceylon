"Given a list of iterable objects, return a new sequence 
 of all elements of the all given objects. If there are
 no arguments, or if none of the arguments contains any
 elements, return the empty sequence."
see (`function expand`)
shared Element[] concatenate<Element>(
        "The iterable objects to concatenate."
        {Element*}* iterables) 
        => [ for (it in iterables) for (val in it) val ];

"Given an iterable object whose elements are also iterable,
 return a new stream with all the elements of the nested
 iterables. If there are no arguments, or if none of the 
 arguments contains any elements, return an empty iterable."
see (`function concatenate`, 
     `function Iterable.chain`)
shared Iterable<Element,OuterAbsent|InnerAbsent>
        expand<Element,OuterAbsent,InnerAbsent>
        (Iterable<Iterable<Element,InnerAbsent>,OuterAbsent> iterables)
        given OuterAbsent satisfies Null
        given InnerAbsent satisfies Null
        => { for (it in iterables) for (val in it) val };