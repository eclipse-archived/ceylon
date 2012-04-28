doc "Abstract supertype of ranged objects which map a range
     of natural numbers to values."
see (Sequence, String)
shared interface Ranged<in Index, out Span> 
        given Index satisfies Comparable<Index> {
    
    doc "Obtain a span containing the mapped values between 
         the two given indexes. If the second given index
         is null, the span has no upper bound."
    shared formal Span span(Index from, Index? to);
    
    doc "Obtain a segment containing the mapped values
         starting from the given index, with the given 
         length."
    shared formal Span segment(Index from, Integer length);
    
}