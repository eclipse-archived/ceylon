shared interface Comparable<in Other> of Other 
        satisfies Equality
        given Other satisfies Comparable<Other> {
    
    doc "The binary compare operator |<=>|. Compares this 
         object with the given object. Implementations must
         respect the constraint that if |x==y| then
         |x<=>y == Comparison.equal|, the constraint that 
         if |x>y| then |y<x|, and the constraint that if 
         |x>y| and |y>z| then |x>z|."
    shared formal Comparison compare(Other other);
    
    doc "The binary |>| operator."
    shared Boolean largerThan(Other other) {
        return compare(other)==larger;
    }
    
    doc "The binary |<| operator."
    shared Boolean smallerThan(Other other) {
        return compare(other)==smaller;
    }
    
    doc "The binary |>=| operator."
    shared Boolean asLargeAs(Other other) {
        return compare(other)!=smaller;
    }
    
    doc "The binary |<=| operator."
    shared Boolean asSmallAs(Other other) {
        return compare(other)!=larger;
    }
    
}