"Returns a function which is the logical 
 disjunction of the given predicate functions."
shared Boolean or<T>(
        Boolean(T) p, 
        Boolean(T) q)(T t) 
    => p(t) || q(t);