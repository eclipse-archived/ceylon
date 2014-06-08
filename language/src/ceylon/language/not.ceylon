"Returns a function which is the logical negation of the 
 given predicate function."
shared Boolean not<in Value>(
    "The predicate function to negate"
    Boolean(Value) p)(Value val) 
        => !p(val);