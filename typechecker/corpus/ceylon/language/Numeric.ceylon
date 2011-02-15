shared interface Numeric<N>
        satisfies Number & Comparable<N>
        given N satisfies Numeric<N> {

    doc "The binary |+| operator"
    shared formal N plus(N number);

    doc "The binary |-| operator"
    shared formal N minus(N number);

    doc "The binary |*| operator"
    shared formal N times(N number);

    doc "The binary |/| operator"
    shared formal N divided(N number);

    doc "The binary |**| operator"
    shared formal N power(N number);
    
}
