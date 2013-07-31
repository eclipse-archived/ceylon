"Abstraction of ordinal types, that is, types with 
 successor and predecessor operations, including
 `Integer` and other `Integral` numeric types.
 `Character` is also considered an ordinal type. 
 `Ordinal` types may be used to generate a `Range`."
see (`Character`, `Integer`, `Integral`, `Range`)
by ("Gavin")
shared interface Ordinal<out Other> of Other
        given Other satisfies Ordinal<Other> {
    
    "The successor of this value."
    shared formal Other successor;
    
    "The predecessor of this value."
    shared formal Other predecessor;
    
}

"Abstraction of ordinal types whose instances can be 
 mapped to the integers or to a range of integers."
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