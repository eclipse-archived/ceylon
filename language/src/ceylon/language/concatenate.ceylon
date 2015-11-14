"Given zero or more argument [[streams|iterables]], return a 
 new sequence containing all elements of every given stream.
 The elements of the resulting stream are ordered first 
 according to the stream in which they occur, and then 
 according to where they occur in that stream. If there are 
 no arguments, or if none of the argument streams contain 
 any elements, return the empty sequence.
 
 For example, the expression
 
     concatenate(1..3, [0.0], {\"hello\", \"world\"})
 
 results in the sequence `[1, 2, 3, 0.0, \"hello\", \"world\"]`
 which has the type `[Integer|Float|String*]`."
see (`function expand`, 
     `function Iterable.chain`,
     `function Sequential.append`)
tagged("Streams")
shared Element[] concatenate<Element>(
        "The streams to concatenate."
        {Element*}* iterables) 
        => [ for (it in iterables) for (val in it) val ];
