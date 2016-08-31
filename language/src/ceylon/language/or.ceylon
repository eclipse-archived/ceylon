"Returns a function which is the logical disjunction of the 
 given predicate functions."
tagged("Functions")
since("1.1.0")
shared Boolean or<in Value>(
    "The first predicate function"
    Boolean(Value) p,
    "The second predicate function"
    Boolean(Value) q)(Value val) 
        => p(val) || q(val);