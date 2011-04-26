shared interface Accumulable<T> 
        given T satisfies Accumulable<T> {

    doc "The binary |+| operator"
    shared formal T plus(T that);
    
}