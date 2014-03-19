"Returns a function which is the logical 
 negation of the given predicate function."
shared Boolean not<T>(
        "The predicate function to negate"
        Boolean(T) p)(T t) 
    => !p(t);