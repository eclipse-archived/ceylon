"Abstract supertype of ranged objects which map a range of 
 [[Comparable]] keys to ranges of values. The type parameter 
 [[Span]] abstracts the type of the resulting range. A 
 subrange may be obtained from an instance of `Ranged` using
 the _span_ and _segment_ operators.
 
 The _span_ operator accepts the first and last indices of 
 the subrange.
 
     print(\"hello world\"[0..5]); //prints \"hello\"
     print(\"hello world\"[6..6]); //prints \"w\"
 
 If the last index is smaller than the first index, the
 subrange is reversed.
 
     print(\"hello world\"[5..0]); //prints \"olleh\"
 
 If the range of indices identified by the first and last
 indices is outside the range of indices of this object, an
 empty subrange is produced.
 
     print(\"hello world\"[-5..-1]); //prints \"\"
     print(\"hello world\"[11..11]); //prints \"\"
 
 The first index may be ommitted, implying that the subrange
 extends forward from the smallest possible index (in this
 case `runtime.minIntegerValue-1`) to the given index.
 
     print(\"hello world\"[...5]) //prints \"hello\"
 
 If the first index is before the first index of this object, 
 an empty subrange is produced. (A reversed subrange is 
 never produced.)
 
     print(\"hello world\"[-5...]); //prints \"\"
 
 The last index may be ommitted, implying that the subrange 
 extends forward from the given index to the largest 
 possible index (in this case `runtime.maxIntegerValue+1`).
 
     print(\"hello world\"[6...]) //prints \"world\"
 
 If the last index is after the last index of this object, 
 an empty subrange is produced. (A reversed subrange is 
 never produced.)
 
     print(\"hello world\"[11...]); //prints \"\"
 
 The _segment_ operator accepts the first index and maximum 
 length of the subrange.
 
     print(\"hello world\"[6:5]) //prints \"world\"
     print(\"hello world\"[6:0]) //prints \"\"
 
 If the length is nonpositive, the subrange is empty. If the
 range of indices identified by the first index and length
 is outside the range of indices of this object, an empty
 subrange is produced. (A reversed subrange is never 
 produced.)
 
     print(\"hello world\"[-3:3]) //prints \"\"
     print(\"hello world\"[11:3]) //prints \"\"
     print(\"hello world\"[6:-3]) //prints \"\""
see (`interface List`, 
     `interface Sequence`, 
     `class String`)
shared interface Ranged<in Index, out Span> of Span {
    
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
