"Returns a function which is the logical 
 exclusive disjunction of the given 
 predicate functions.
 Unlike [[and]] and [[or]] both predicate functions are always evaluated."
shared Boolean xor<T>(
        Boolean(T) p, 
        Boolean(T) q)(T t) { 
    if (p(t)) {
        return !q(t);
    } else {
        return q(t);
    }
}