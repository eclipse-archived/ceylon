doc "The general contract for values whose magnitude can be 
     compared."
by "Gavin"
shared interface Comparable<in Other> of Other 
        satisfies Equality
        given Other satisfies Comparable<Other> {
    
    doc "Compares this value with the given value. 
         Implementations must respect the constraint that 
         `x==y` if and only if `x<=>y == equal`, the constraint 
         that if `x>y` then `y<x` (symmetry), and the constraint 
         that if `x>y` and `y>z` then `x>z` (transitivity)."
    shared formal Comparison compare(Other other);
    
    doc "Determines if this value is strictly larger than 
         the given value."
    shared Boolean largerThan(Other other) {
        return compare(other)==larger;
    }
    
    doc "Determines if this value is strictly smaller than 
         the given value."
    shared Boolean smallerThan(Other other) {
        return compare(other)==smaller;
    }
    
    doc "Determines if this value is larger than or equal to
         the given value."
    shared Boolean asLargeAs(Other other) {
        return compare(other)!=smaller;
    }
    
    doc "Determines if this value is smaller than or equal 
         to the given value."
    shared Boolean asSmallAs(Other other) {
        return compare(other)!=larger;
    }
    
}