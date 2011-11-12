doc "Abstract supertype of ranged objects which map a range
     of natural numbers to values."
see (Sequence, List, String)
shared interface Ranged<out Span> {
    
    doc "Obtain a span containing the mapped values between 
         the two given indexes."
    shared formal Span span(Natural from, Natural to);
    
    doc "Obtain a segment containing the mapped values
         starting from the given index, with the given 
         length."
    shared formal Span segment(Natural from, Natural length);
    
}