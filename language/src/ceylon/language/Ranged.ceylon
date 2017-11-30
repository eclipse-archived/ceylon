/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"Abstract supertype of _ranged streams_ mapping a range of 
 discrete indices to elements of the stream, and supporting 
 operations that produce a subrange of indexed elements. The 
 type parameter [[Subrange]] abstracts the type of the 
 resulting subrange. A subrange may be obtained from an 
 instance of `Ranged` using the _span_ and _measure_ 
 operators.
 
 Typically, the `Subrange` type is the same as the ranged
 type itself. But this is not required. For example, the
 subranges of a [[StringBuilder]] are [[String]]s.
 
 Often, in a [[List]] or sorted map for example, an index
 and its element are distinct values. Sometimes, in a sorted 
 set for example, the index and element are identical.
 
 The _span_ operator accepts the first and last indices of 
 the subrange.
 
     print(\"hello world\"[0..4]); //prints \"hello\"
     print(\"hello world\"[6..6]); //prints \"w\"
 
 If the last index is smaller than the first index, the
 subrange is reversed.
 
     print(\"hello world\"[4..0]); //prints \"olleh\"
 
 If the range of indices identified by the first and last
 indices is outside the range of indices of this object, an
 empty subrange is produced.
 
     print(\"hello world\"[-5..-1]); //prints \"\"
     print(\"hello world\"[11..11]); //prints \"\"
 
 The first index may be omitted, implying that the subrange
 extends forward from the smallest possible index (in this
 case `runtime.minIntegerValue`) to the given index.
 
     print(\"hello world\"[...4]) //prints \"hello\"
 
 If the first index is before the first index of this object, 
 the first index of the object is used.
 
     print(\"hello world\"[-5...]); //prints \"hello world\"
 
 The last index may be omitted, implying that the subrange 
 extends forward from the given index to the largest 
 possible index (in this case `runtime.maxIntegerValue`).
 
     print(\"hello world\"[6...]) //prints \"world\"
 
 If the last index is after the last index of this object, 
 an empty subrange is produced. (A reversed subrange is 
 never produced.)
 
     print(\"hello world\"[11...]); //prints \"\"
 
 The _measure_ operator accepts the first index and maximum 
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
 
 The span and measure operations must be consistent. That is, 
 for every pair of indices `x` and `y` in the ranged object 
 `ranged` such that `y` does not occur before `x` and the 
 span `ranged[x..y]` has length `n`:
 
 - `ranged[x..y]==ranged[x:n]`."
see (interface List, 
     interface Sequence, 
     class String, 
     class StringBuilder)
shared interface Ranged<in Index, out Element, out Subrange>
        satisfies {Element*}
        given Subrange satisfies Ranged<Index,Element,Subrange> {
    
    "Obtain a span containing the elements between the two 
     given indices.
     
     For any ranged stream `r`, `r.span(from, to)` may be
     written using the span operator:
     
         r[from..to]
     
     The span should contain elements of this stream, 
     starting from the element at the given [[starting 
     index|from]], and ending with the element at the given 
     [[ending index|to]], in the same order as they are 
     produced by the [[iterator]] of the stream, except when 
     the ending index occurs earlier than the starting index, 
     in which case they occur in the opposite order.
     
     When one or both of the given indices does not belong 
     to this ranged stream, the behavior is implementation 
     dependent."
    shared formal Subrange span(Index from, Index to);
    
    "Obtain a span containing the elements between the given
     [[starting index|from]] and the last index of this 
     ranged object.
     
     For any ranged stream `r`, `r.spanFrom(from)` may be
     written using the span operator:
     
         r[from...]
     
     The span should contain elements of this stream, 
     starting from the element at the given [[starting 
     index|from]], in the same order as they are produced by 
     the [[iterator]] of the stream.
     
     When the given index does not belong to this ranged 
     stream, the behavior is implementation dependent."
    shared formal Subrange spanFrom(Index from);
    
    "Obtain a span containing the elements between the first 
     index of this ranged stream and given [[end index|to]].
     
     For any ranged stream `r`, `r.spanTo(to)` may be
     written using the span operator:
     
         r[...to]
     
     The span should contain elements of this stream, up to 
     the element at the given [[ending index|to]], in the 
     same order as they are produced by the [[iterator]] of
     the stream.
     
     When the given index does not belong to this ranged 
     stream, the behavior is implementation dependent."
    shared formal Subrange spanTo(Index to);
    
    "Obtain a measure containing the mapped values starting 
     from the given [[starting index|from]], with the given 
     [[length]]. If `length<=0`, the resulting measure is 
     empty.
     
     For any ranged stream `r`, `r.measure(from, length)` 
     may be written using the measure operator:
     
         r[from:length]
     
     The measure should contain the given [[number|length]] 
     of elements of this stream, starting from the element 
     at the given [[starting index|from]], in the same order 
     as they are produced by the [[iterator]] of the stream. 
     In the case where the iterator would be exhausted 
     before [[length]] elements are produced, the resulting 
     measure contains only those elements which were 
     produced before the iterator was exhausted, and the 
     length of the measure is less then the given `length`.
     
     When the given index does not belong to this ranged 
     object, the behavior is implementation dependent."
    since("1.1.0")
    shared formal Subrange measure(Index from, Integer length);
    
}
