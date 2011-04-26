shared interface Summable<T> 
        given T satisfies Summable<T> {

    doc "The binary |+| operator"
    shared formal T plus(T that);
    
}