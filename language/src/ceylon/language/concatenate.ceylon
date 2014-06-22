"Given a list of [[streams|iterables]], return a new sequence 
 containing all elements of every given stream. If there are
 no arguments, or if none of the arguments contains any
 elements, return the empty sequence.
 
     [Integer|Float|String*] stuff = concatenate(1..3, [0.0], {\"hello\", \"world\"});"
see (`function expand`, 
     `function List.append`)
shared Element[] concatenate<Element>(
        "The iterable objects to concatenate."
        {Element*}* iterables) 
        => [ for (it in iterables) for (val in it) val ];

"Given a [[stream|iterables]] whose elements are also 
 streams, return a new stream with all elements of every 
 nested stream. If there are no nested streams, or if all of
 the nested streams are empty, return an empty stream.
 
     {Address*} addresses = expand { for (m in members) if (m.active) m.addresses };"
see (`function concatenate`, 
     `function Iterable.chain`)
shared Iterable<Element,OuterAbsent|InnerAbsent>
        expand<Element,OuterAbsent,InnerAbsent>
        (Iterable<Iterable<Element,InnerAbsent>,OuterAbsent> iterables)
        given OuterAbsent satisfies Null
        given InnerAbsent satisfies Null
        => { for (it in iterables) for (val in it) val };