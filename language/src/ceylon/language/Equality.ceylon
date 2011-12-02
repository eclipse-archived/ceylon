doc "The general contract for values which can be compared
     for equality. Note that some values which can be 
     compared for equality do not have a well-defined 
     identity, but all values which have identity can be 
     compared for equality."
see (Comparable, IdentifiableObject)
by "Gavin"
shared interface Equality {
    
    doc "Determine if two values are equal. Implementations 
         should respect the constraint that if `x===y` then 
         `x==y` (reflexivity), the constraint that if `x==y` 
         then `y==x` (symmetry), and the constraint that if 
         `x==y` and `y==z` then `x==z` (transitivity). 
         Furthermore it is recommended that implementations 
         ensure that if `x==y` then `x` and `y` have the 
         same concrete class."
    shared formal Boolean equals(Equality that);
    
    doc "The hash value of the value, which allows the value
         to be an element of a hash-based set or key of a 
         hash-based map. Implementations must respect the 
         constraint that if `x==y` then `x.hash==y.hash`."
    shared formal Integer hash;
    
}
