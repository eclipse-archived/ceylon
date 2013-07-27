"Abstract supertype of ranged objects which map a range
 of `Comparable` keys to ranges of values. The type
 parameter `Span` abstracts the type of the resulting
 range.
 
 A span may be obtained from an instance of `Ranged`
 using the span operator:
 
     print(\"hello world\"[0..5])
 "
//see (`List`, `Sequence`, `String`)
shared interface Ranged<in Index, out Span> of Span
        given Index satisfies Comparable<Index> {
    
    "Obtain a span containing the mapped values between 
     the two given indices."
    shared formal Span span(Index from, Index to);
   
    "Obtain a span containing the mapped values between
     the starting index and the end of the receiver."
    shared formal Span spanFrom(Index from);

    "Obtain a span containing the mapped values between
     the start of the receiver and the end index."
    shared formal Span spanTo(Index to);
 
    "Obtain a segment containing the mapped values
     starting from the given index, with the given 
     length."
    shared formal Span segment(Index from, Integer length);
    
}
