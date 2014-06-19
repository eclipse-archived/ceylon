"Abstraction of [[ordinal types|Ordinal]] whose values may 
 be used as endpoints of a [[Range]]. A range may be 
 specified using:
 
 - the _span operator_, written `first..last`, or 
 - the _segment operator_, written `first:length`.
 
 The span operator accepts the first and last values of 
 the range.
 
     0..5    // [0, 1, 2, 3, 4, 5]
     0..0    // [0]
 
 If the last value is smaller than the first value, the
 range is reversed.
 
     5..0    // [5, 4, 3, 2, 1, 0]
     0..-5   // [0, -1, -2, -3, -4, -5]
 
 The segment operator accepts the first index and maximum 
 length of the range.
 
     0:5     // [0, 1, 2, 3, 4]
 
 If the length is nonpositive, the subrange is empty.
 
     0:0     // []
     5:0     // []
     0:-5    // []"
see (`class Range`)
shared interface Enumerable<Other> of Other
        satisfies Ordinal<Other>
        given Other satisfies Enumerable<Other> {
    
    "The indirect successor or predecessor at the given
     [[offset]], where:
     
     - `x.neighbour(0) == x`,
     - `x.neighbour(i+1) == x.neighbour(i).successor`, and
     - `x.neighbour(i-1) == x.neighbour(i).predecessor`."
    shared formal Other neighbour(Integer offset);
    
    shared actual default Other successor => neighbour(1);
    shared actual default Other predecessor => neighbour(-1);
    
    "Compute the offset from the given value, where:
     
     - `x.offset(x) == 0`,
     - `x.successor.offset(x) == 1`,
     - `x.predecessor.offset(x) == -1`,
     - `x.neighbour(n).offset(x) == n`,
     - `x.offset(y) == -y.offset(x)`, and
     - `x.offset(y) == x.offset(z) + z.offset(y)`
     
     unless this `Enumerable` type has a circular structure."
    shared formal Integer offset(Other other);
    
    "The sign of the offset from the given value."
    shared default Integer offsetSign(Other other)
            => offset(other).sign;
    
}
