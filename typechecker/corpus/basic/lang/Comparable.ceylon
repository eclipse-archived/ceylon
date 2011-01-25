shared interface Comparable<in T> 
        satisfies Equality<T> & PartlyComparable<T>
        given T satisfies Comparable<T> {
    
    doc "The binary compare operator |<=>|. Compares this 
         object with the given object. Implementations must
         respect the constraint that if |x==y| then
         |x<=>y == Comparison.equal|, the constraint that 
         if |x>y| then |y<x|, and the constraint that if 
         |x>y| and |y>z| then |x>z|."
    shared actual formal Comparison compare(T other);
    
    doc "The binary |>| operator."
    shared Boolean largerThan(T other) {
        return compare(other)==larger
    }
    
    doc "The binary |<| operator."
    shared Boolean smallerThan(T other) {
        return compare(other)==smaller
    }
    
    doc "The binary |>=| operator."
    shared Boolean asLargeAs(T other) {
        return compare(other)!=smaller
    }
    
    doc "The binary |<=| operator."
    shared Boolean asSmallAs(T other) {
        return compare(other)!=larger
    }
    
}