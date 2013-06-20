"The general contract for values whose magnitude can be 
 compared. `Comparable` imposes a total ordering upon
 instances of any type that satisfies the interface.
 If a type `T` satisfies `Comparable<T>`, then instances
 of `T` may be compared using the comparison operators
 `<`, `>`, `<=`, >=`, and `<=>`.
 
 The total order of a type must be consistent with the 
 definition of equality for the type. That is, there
 are three mutually exclusive possibilities:
 
 - `x<y`,
 - `x>y`, or
 - `x==y`"
by ("Gavin")
shared interface Comparable<in Other> of Other 
        given Other satisfies Comparable<Other> {
    
    "Compares this value with the given value. 
     Implementations must respect the constraints that: 
     
     - `x==y` if and only if `x<=>y == equal` 
        (consistency with `equals()`), 
     - if `x>y` then `y<x` (symmetry), and 
     - if `x>y` and `y>z` then `x>z` (transitivity)."
    see (`equals`)
    shared formal Comparison compare(Other other);
    
    /*"Determines if this value is strictly larger than 
     the given value."
    shared Boolean largerThan(Other other) {
        return compare(other)==larger;
    }
    
    "Determines if this value is strictly smaller than 
     the given value."
    shared Boolean smallerThan(Other other) {
        return compare(other)==smaller;
    }
    
    "Determines if this value is larger than or equal to
     the given value."
    shared Boolean asLargeAs(Other other) {
        return compare(other)!=smaller;
    }
    
    "Determines if this value is smaller than or equal 
     to the given value."
    shared Boolean asSmallAs(Other other) {
        return compare(other)!=larger;
    }*/
    
}