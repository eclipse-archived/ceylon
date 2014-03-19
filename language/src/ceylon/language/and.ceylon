"Returns a function which is the logical 
 conjunction of the given predicate functions."
shared Boolean and<T>(
        Boolean(T) p, 
        Boolean(T) q)(T t) 
    => p(t) && q(t);