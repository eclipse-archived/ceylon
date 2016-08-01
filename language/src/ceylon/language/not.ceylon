"Returns a function which is the logical negation of the 
 given predicate function."
tagged("Functions")
since("1.1.0")
shared Boolean not<in Value>(
    "The predicate function to negate"
    Boolean(Value) p)(Value val) 
        => !p(val);