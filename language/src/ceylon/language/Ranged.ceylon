doc "Abstract supertype of ranged objects which map a range
     of `Comparable` keys to ranges of values. The type
     parameter `Span` abstracts the type of the resulting
     range.
     
     A span may be obtained from an instance of `Ranged`
     using the span operator:
     
         print(\"hello world\"[0..5])
     "
see (List, Sequence, String)
shared interface Ranged<in Index, out Span> 
        given Index satisfies Comparable<Index> {
    
    doc "Obtain a span containing the mapped values between 
         the two given indices. If the second given index
         is null, the span has no upper bound."
    shared formal Span span(Index from, Index? to);
    
    doc "Obtain a segment containing the mapped values
         starting from the given index, with the given 
         length."
    shared formal Span segment(Index from, Integer length);
    
}