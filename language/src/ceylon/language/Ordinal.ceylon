"Abstraction of ordinal types, that is, types with 
 [[successor]] and [[predecessor]] operations, including 
 [[Integer]] and other [[Integral]] numeric types.
 [[Character]] is also considered an ordinal type.
 
 The _increment_ operator `++` and _decrement_ operator `--`
 are defined for all `Ordinal` types.
 
     function increment() {
         count++;
     }
 
 An `Ordinal` type which is also [[Comparable]] may be used 
 to generate a [[Range]], using the span or segment 
 operators.
 
 The _span_ operator `..` accepts the first and last values 
 of the range.
 
     0..5    // [0, 1, 2, 3, 4, 5]
     0..0    // [0]
 
 If the last value is smaller than the first value, the
 range is reversed.
 
     5..0    // [5, 4, 3, 2, 1, 0]
     0..-5   // [0, -1, -2, -3, -4, -5]
 
 The _segment_ operator `:` accepts the first index and 
 maximum length of the subrange.
 
     0:5     // [0, 1, 2, 3, 4]
 
 If the length is nonpositive, the subrange is empty.
 
     0:0     // []
     5:0     // []
     0:-5    // []
 
 Most `Ordinal` types are also [[Enumerable]]."
see (`class Character`, 
     `class Integer`, 
     `interface Integral`, 
     `interface Enumerable`,
     `class Range`)
by ("Gavin")
shared interface Ordinal<out Other> of Other
        given Other satisfies Ordinal<Other> {
    
    "The successor of this value."
    shared formal Other successor;
    
    "The predecessor of this value."
    shared formal Other predecessor;
    
}

"Abstraction of [[ordinal types|Ordinal]] whose instances 
 can be mapped to the [[integers|Integer]] or to a range of 
 integers."
shared interface Enumerable<out Other> of Other
        satisfies Ordinal<Other> 
        given Other satisfies Enumerable<Other> {
        
    "The corresponding integer. The implementation must
     satisfy these constraints:
    
         (x.successor).integerValue = x.integerValue+1
         (x.predecessor).integerValue = x.integerValue-1
     
     for every instance `x` of the enumerable type."
    shared formal Integer integerValue;
    
}