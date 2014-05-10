"Abstract supertype of _ranged objects_ mapping a range of 
 discrete indices to elements, and supporting operations 
 that produce a subrange of indexed elements. The type 
 parameter [[Span]] abstracts the type of the resulting 
 subrange. A subrange may be obtained from an instance of 
 `Ranged` using the _span_ and _segment_ operators.
 
 Often, in a [[List]] or sorted map for example, an index
 and its element are distinct values. Sometimes, in a sorted 
 set for example, the index and element are identical.
 
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
     print(\"hello world\"[6:-3]) //prints \"\"
 
 The span and segment operations must be consistent. That is, 
 for every pair of indices `x` and `y` in the ranged object 
 `ranged` such that `y` does not occur before `x` and the 
 span `ranged[x..y]` has length `n`:
 
 - `ranged[x..y]==ranged[x:n]`.
 
 Many ranged objects are also instances of 
 [[Correspondence]]. In this case, the span and segment
 operations should be consistent with the item operation. 
 That is, for every index `x` in the ranged object `ranged`:
 
 - `ranged[x..x].first==ranged[x]`, and
 - `ranged[x:1].first==ranged[x]`."
see (`interface List`, 
     `interface Sequence`, 
     `class String`)
shared interface Ranged<in Index, out Element, out Span> 
        of Span
        satisfies {Element*}
        given Span satisfies Ranged<Index,Element,Span> {
    
    "Obtain a span containing the elements between the two 
     given indices. 
     
     The span should contain elements of this iterable 
     object, starting from the element at the given 
     [[starting index|from]], and ending with the element at 
     the given [[ending index|to]], in the same order as 
     they are produced by the [[iterator]], except when the 
     ending index occurs earlier than the starting index, in 
     which case they occur in the opposite order.
     
     When one or both of the given indices does not belong 
     to this ranged object, the behavior is implementation 
     dependent."
    shared formal Span span(Index from, Index to);
   
    "Obtain a span containing the elements between the given
     [[starting index|from]] and the last index of this 
     ranged object.
     
     The span should contain elements of this iterable 
     object, starting from the element at the given 
     [[starting index|from]], in the same order as they are 
     produced by the [[iterator]].
     
     When the given index does not belong to this ranged 
     object, the behavior is implementation dependent."
    shared formal Span spanFrom(Index from);

    "Obtain a span containing the elements between the first 
     index of this ranged object and given [[end index|to]].
     
     The span should contain elements of this iterable 
     object, up to the element at the given 
     [[ending index|to]], in the same order as they are 
     produced by the [[iterator]].
     
     When the given index does not belong to this ranged 
     object, the behavior is implementation dependent."
    shared formal Span spanTo(Index to);
 
    "Obtain a segment containing the mapped values starting 
     from the given [[starting index|from]], with the given 
     [[length]]. If `length<=0`, the resulting segment is 
     empty.
     
     The segment should contain the given [[number|length]] 
     of elements of this iterable object, starting from the
     element at the given [[starting index|from]], in the 
     same order as they are produced by the [[iterator]]. In
     the case where the iterator would be exhausted before
     [[length]] elements are produced, the resulting segment
     contains only those elements which were produced before
     the iterator was exhausted, and the length of the 
     segment is less then the given `length`.
     
     When the given index does not belong to this ranged 
     object, the behavior is implementation dependent."
    shared formal Span segment(Index from, Integer length);
    
}
